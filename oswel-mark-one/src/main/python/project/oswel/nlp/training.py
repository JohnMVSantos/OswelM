from src.main.python.project.oswel.nlp.settings import INTENTS_PATH, \
    WORDS_PATH, CLASS_PATH, MODEL_PATH
from tensorflow.keras.optimizers.schedules import ExponentialDecay
from src.main.python.project.oswel.logger import logger
from tensorflow.keras.layers import Dense, Dropout
from tensorflow.keras.models import Sequential
from nltk.stem import WordNetLemmatizer
import numpy as np
import argparse
import random
import json
import nltk
import os


class TrainOswelNLP:
    """
    Trains a Keras NLP model based on the provided intents.

    Parameters
    ----------
        model_path: str
            The path to save the Keras NLP model.

        words_path: str
            The path to save the words.txt file.

        class_path: str
            The path to save the classes.txt file.

        intents_path: str
            The path to the intents.json file.

        epochs: int
            The number of epochs to train the model.

    Raises
    ------
        ValueError
            Raised if path to the intents.json does not exist.
    """
    def __init__(
            self,
            model_path=MODEL_PATH,
            words_path=WORDS_PATH,
            class_path=CLASS_PATH,
            intents_path=INTENTS_PATH,
            epochs=200
        ):

        self.intents = self.load_intents(intents_path)
        self.model_path = model_path
        self.words_path = words_path
        self.class_path = class_path
        self.epochs = epochs
        self.lemmatizer = WordNetLemmatizer()

    @staticmethod
    def load_intents(intents_path):
        """
        Reads the intents JSON file.

        Parameters
        ----------
            intents_path: str
                The path to the intents.json file.

        Returns
        -------
            intents: dict
                The contents of the JSON file.

        Raises
        ------
            ValueError
                Raised if the path to the JSON file does not exist.
        """
        if os.path.exists(intents_path):
            return json.loads(open(intents_path).read())
        else:
            raise ValueError(
                "The given intents path does not exist at: {}".format(
                intents_path))

    @staticmethod 
    def save_resource(file_path, resources):
        """
        Saves the resources into text files which will be needed \
            for model deployement.

        Parameters
        ---------
            file_path: str
                The path to save the resource file.

            resources: list or np.ndarray
                An array usually containing words or classes.

        Returns
        -------
            None

        Raises
        ------
            None
        """
        with open(file_path, "w") as fp:
            for resource in resources:
                fp.write("{}\n".format(resource))
        fp.close()
    
    def process_resources(self):
        """
        Processes training data into the proper formats that \
            will be used for training.

        Parameters
        ----------
            None

        Returns
        -------
           train_x

           train_y

        Raises
        ------
            None
        """
        words, classes, documents = list(), list(), list()
        ignore_letters = ['?', "!", ".", ","]
        
        for intent in self.intents["intents"]:
            for pattern in intent["patterns"]:
                word_list = nltk.word_tokenize(pattern)
                words.extend(word_list)
                documents.append((word_list, intent["tag"]))
                if intent["tag"] not in classes:
                    classes.append(intent["tag"])

        words = [self.lemmatizer.lemmatize(word).lower() for word in 
                    words if word not in ignore_letters]
        words = sorted(set(words))
        classes = sorted(set(classes))

        self.save_resource(self.words_path, words)
        self.save_resource(self.class_path, classes)

        output_empty = [0] * len(classes)
        training = list()

        for document in documents:
            bag = list()
            word_patterns = document[0]
            word_patterns = [self.lemmatizer.lemmatize(
                word.lower()) for word in word_patterns]
            for word in words:
                bag.append(1) if word in word_patterns else bag.append(0)

            output_row = list(output_empty)
            output_row[classes.index(document[1])] = 1
            training.append([bag, output_row])

        random.shuffle(training)
        training = np.array(training)
        train_x = list(training[:, 0])
        train_y = list(training[:, 1])
        return train_x, train_y

    def train(self, train_x, train_y):
        """
        Trains the NLP model provided with the training data. The model \
            is saved as a Keras H5 file. 

        Parameters
        ----------
            train_x

            train_y

        Returns
        -------
            None

        Raises
        ------
            None
        """
        # Building the neural network
        model = Sequential()
        model.add(Dense(128, input_shape=(len(train_x[0]),), activation="relu"))
        model.add(Dropout(0.5))
        model.add(Dense(64, activation="relu"))
        model.add(Dropout(0.5))
        model.add(Dense(len(train_y[0]), activation="softmax"))

        lr_schedule = ExponentialDecay(
            initial_learning_rate=0.01,
            decay_steps=10000,
            decay_rate=0.9)

        model.compile(
            loss="categorical_crossentropy", 
            optimizer='sgd', 
            metrics=["accuracy"])
        hist = model.fit(
            np.array(train_x), 
            np.array(train_y), 
            epochs=self.epochs, 
            batch_size=5, 
            verbose=1)
        model.save(self.model_path, hist)
        logger("Training complete!", code="SUCCESS")
        logger("Model file can be found in {}".format(self.model_path), 
               code="INFO")
        
if __name__ == '__main__':

    parser = argparse.ArgumentParser(
        description=("OswelNLP Model Deployment"),
        formatter_class=argparse.RawTextHelpFormatter
    )
    parser.add_argument('-m', '--model',
                        help=("The path to save the Keras NLP model."),
                        type=str,
                        default=MODEL_PATH
                        )
    parser.add_argument('-w', '--words',
                        help=("The path to save the words.txt"),
                        type=str,
                        default=WORDS_PATH
                        )
    parser.add_argument('-c', '--classes',
                        help=("The path to save the classes.txt"),
                        type=str,
                        default=CLASS_PATH
                        )
    parser.add_argument('-i', '--intents',
                        help=("The path to save the intents.json"),
                        type=str,
                        default=INTENTS_PATH
                        )
    parser.add_argument('-e', '--epochs',
                        help=("The number of epochs to train"),
                        type=int,
                        default=200
                        )
    args = parser.parse_args()

    trainer = TrainOswelNLP(
        model_path=args.model,
        words_path=args.words,
        class_path=args.classes,
        intents_path=args.intents,
        epochs=args.epochs
    )
    train_x, train_y = trainer.process_resources()
    trainer.train(train_x, train_y)