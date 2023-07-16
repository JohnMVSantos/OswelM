from src.main.python.project.oswel.exceptions import UnrecognizedTaskException
from src.main.python.project.oswel.nlp.training import TrainOswelNLP
from src.main.python.project.oswel.nlp.deploy import DeployOswelNLP
from src.main.python.project.oswel.nlp.settings import MODEL_PATH, \
    WORDS_PATH, CLASS_PATH, INTENTS_PATH

import argparse

def main():
    parser = argparse.ArgumentParser(
            description=("OswelNLP Model Processes"),
            formatter_class=argparse.RawTextHelpFormatter
        )
    parser.add_argument('-t', '--task',
                        help=("The task to perform: train or deploy the Keras",
                              "NLP model."),
                        type=str,
                        choices=['train', 'deploy'],
                        required=True
                        )
    parser.add_argument('-m', '--model',
                        help=("The path to save or points the Keras NLP model."),
                        type=str,
                        default=MODEL_PATH
                        )
    parser.add_argument('-w', '--words',
                        help=("The path to save or points to the words.txt"),
                        type=str,
                        default=WORDS_PATH
                        )
    parser.add_argument('-c', '--classes',
                        help=("The path to save or points to the classes.txt"),
                        type=str,
                        default=CLASS_PATH
                        )
    parser.add_argument('-i', '--intents',
                        help=("The path that points to the intents.json"),
                        type=str,
                        default=INTENTS_PATH
                        )
    parser.add_argument('-e', '--epochs',
                        help=("The number of epochs to train"),
                        type=int,
                        default=200
                        )
    parser.add_argument('-s', '--score',
                        help=("The score threshold to consider predictions"),
                        type=float,
                        default=0.25
                        )
    args = parser.parse_args()

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
        print("Deploying Model. Press 'CTRL-C' to exit.")
        while True:
            message = input("Sentence: ")
            ints = deploy.predict_class(message)
            res = deploy.get_response(ints)
            print(f"Response: {res}")
    else:
        raise UnrecognizedTaskException(args.task)

if __name__ == '__main__':
    main()