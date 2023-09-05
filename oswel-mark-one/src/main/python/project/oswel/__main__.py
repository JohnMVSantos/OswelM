from src.main.python.project.oswel.exceptions import UnrecognizedTaskException
from src.main.python.project.oswel.nlp.training import TrainOswelNLP
from src.main.python.project.oswel.nlp.deploy import DeployOswelNLP
from src.main.python.project.oswel.nlp.settings import MODEL_PATH, \
    WORDS_PATH, CLASS_PATH, INTENTS_PATH
from src.main.python.project.oswel.startup import StartApp
from src.main.python.project.oswel.logger import logger
import argparse

def main():
    parser = argparse.ArgumentParser(description=("OswelNLP Model Processes"))
    parser.add_argument('-t', '--task',
                        help="The task to perform: train or deploy the Keras NLP model.",
                        type=str,
                        choices=['train', 'deploy']
                        )
    parser.add_argument('-m', '--model',
                        help="The path to save or points the Keras NLP model.",
                        type=str,
                        default=MODEL_PATH
                        )
    parser.add_argument('-w', '--words',
                        help="The path to save or points to the words.txt",
                        type=str,
                        default=WORDS_PATH
                        )
    parser.add_argument('-c', '--classes',
                        help="The path to save or points to the classes.txt",
                        type=str,
                        default=CLASS_PATH
                        )
    parser.add_argument('-i', '--intents',
                        help="The path that points to the intents.json",
                        type=str,
                        default=INTENTS_PATH
                        )
    parser.add_argument('-e', '--epochs',
                        help="The number of epochs to train",
                        type=int,
                        default=200
                        )
    parser.add_argument('-s', '--score',
                        help="The score threshold to consider predictions",
                        type=float,
                        default=0.25
                        )
    parser.add_argument('-v', '--voice', 
                        help="The voice to use to speak.",
                        type=str,
                        default="en-GB-RyanNeural"
                        )
    parser.add_argument('-f', '--file_path',
                        help="The path to save the temporary MP3 file.",
                        type=str,
                        default="oswelAudio.mp3"
                        )
    args = parser.parse_args()

    if args.task is not None:
        if args.task.lower() == "train":
            trainer = TrainOswelNLP(
                model_path=args.model,
                words_path=args.words,
                class_path=args.classes,
                intents_path=args.intents,
                epochs=args.epochs
            )
            train_x, train_y = trainer.process_resources()
            trainer.train(train_x, train_y)

        elif args.task.lower() == "deploy":
            deploy = DeployOswelNLP(
                model_path=args.model,
                words_path=args.words,
                class_path=args.classes,
                intents_path=args.intents,
                score_threshold=args.score
            )
            logger("Deploying Model. Press 'CTRL-C' to exit.", code="INFO")
            while True:
                message = input("Sentence: ")
                ints = deploy.predict_class(message)
                res = deploy.get_response(ints)
                logger(f"Response: {res}", code="INFO")
        else:
            raise UnrecognizedTaskException(args.task)
    else:
        deploy = DeployOswelNLP(
            model_path=args.model,
            words_path=args.words,
            class_path=args.classes,
            intents_path=args.intents,
            score_threshold=args.score
        )
        start = StartApp(deploy)
        start.start_process(voice=args.voice, file=args.file_path)

if __name__ == '__main__':
    main()