
import tensorflow as tf
from PIL import Image
import numpy as np
import os

class DeployOswelFaceDetection:

    def __init__(
            self,
            model_path,
            iou_threshold=0.45,
            score_threshold=0.70,
            norm="unsigned",
            max_detections=2,
            label_offset=1
    ):
        if os.path.exists(model_path):
            self.model = self.load_model(model_path)
        else:
            raise ValueError("The provided path to the model does not exist.")
        
        self.iou_threshold=self.clamp(iou_threshold)
        self.score_threshold=self.clamp(score_threshold)
        self.max_detections = max_detections
        self.label_offset=label_offset

        if norm.lower() in ["signed", "unsigned", "raw"]:
            self.norm = norm.lower()
        else:
            raise ValueError(
                "Can only support raw, unsigned, signed normalizations.")    
    
    @staticmethod
    def load_model(model_path):
        return tf.keras.models.load_model(model_path, compile=False)

    @staticmethod
    def clamp(value, min=0.0, max=1.0):
        return min if value < min else max if value > max else value
    
    def apply_normalization(self, image):
        if self.norm == 'signed':
            return np.expand_dims((image / 127.5) - 1.0, 0).astype(np.float32)
        elif self.norm == 'unsigned':
            return np.expand_dims(image / 255.0, 0).astype(np.float32)
        else:
            return np.expand_dims(image, 0).astype(np.float32)
        
    @staticmethod
    def resize(image, size=None):
        """
        # Resize method requires (width, height)
        """
        if size is None:
            return image
        else:
            if isinstance(image, str):
                if os.path.exists(image):
                    image = Image.open(image).resize(size)
                else:
                    raise ValueError(
                        "The given image path does not exist at {}".format(
                            image))
            elif isinstance(image, np.ndarray):
                image = Image.fromarray(np.uint8(image)).resize(size)
            else:
                raise ValueError("The image provided is neither a " +
                                    "numpy array or a pillow image object. " +
                                    "Recieved type: {}".format(type(image)))
            return np.asarray(image)
        
    def get_input_shape(self):
        # This should roll the values from (height, width) to (width, height).
        return np.flip(self.model.input.shape[1:])[1:]
    
    def run_single_instance(self, image):
        image = self.resize(image, self.get_input_shape())
        image = self.apply_normalization(image)
        outputs = self.model.predict(image, verbose=0)
        return self.apply_nms(outputs)
    
    def apply_nms(self, outputs):
        boxes = outputs[-2]
        if self.label_offset > 0:
            scores = outputs[-1][..., self.label_offset:]
        else:
            scores = outputs[-1]

        nmsed_boxes, nmsed_scores, nmsed_classes, valid_boxes = \
            tf.image.combined_non_max_suppression(
                boxes,
                scores,
                self.max_detections,
                self.max_detections,
                iou_threshold=self.iou_threshold,
                score_threshold=self.score_threshold,
                clip_boxes=True
            )

        nmsed_boxes = nmsed_boxes.numpy()
        nmsed_classes = tf.cast(nmsed_classes, tf.int32)

        nms_predicted_boxes = [nmsed_boxes[i, :valid_boxes[i], :]
                               for i in range(nmsed_boxes.shape[0])][0]
        nms_predicted_classes = [nmsed_classes.numpy()[i, :valid_boxes[i]]
                                 for i in range(nmsed_classes.shape[0])][0]
        nms_predicted_scores = [nmsed_scores.numpy()[i, :valid_boxes[i]]
                                for i in range(nmsed_scores.shape[0])][0]
        return nms_predicted_boxes, nms_predicted_classes, nms_predicted_scores

if __name__ == '__main__':

    from src.main.python.project.oswel.objectdetection.settings import MODEL_PATH
    import cv2
    video = cv2.VideoCapture(0, cv2.CAP_DSHOW)
    model = DeployOswelFaceDetection(
        model_path=MODEL_PATH)

    if video.isOpened():
        # Capture the video frame
        ret, frame = video.read()
        if ret:
            detection = model.run_single_instance(frame)
            print(detection)
        else:
            raise RuntimeError("Reading frame is unsuccesful.")
    else:
        raise RuntimeError("Camera is not opened.")