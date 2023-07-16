from src.main.python.project.oswel.nlp.settings import MODEL_PATH, \
    CLASS_PATH, WORDS_PATH, INTENTS_PATH
from tensorflow.keras.models import load_model
from nltk.stem import WordNetLemmatizer
import numpy as np
import argparse
import pickle
import random
import json
import nltk
import os

class DeployOswelNLP:
    """
    Deploys a Keras NLP trained model in python. However, this project \
        uses this class for testing purposes and model is deployed in \
            practice using the JAVA application. 

    Parameters
    ----------
        model_path: str
            The path to the saved model file.

        words_path: str
            The path to the saved words.txt file.

        class_path: str
            The path to the saved class.txt file.

        intents_path: str
            The path to the saved intents.json file.
            This was used to train the model.

        score_threshold: float
            The score threshold to consider for the predictions.
    
    Raises
    ------
        ValueError
            Raises if any of the provided paths does not exist. 
    """

    def __init__(
        self,
        model_path=MODEL_PATH,
        class_path=CLASS_PATH,
        words_path=WORDS_PATH,
        intents_path=INTENTS_PATH,
        score_threshold=0.25
        ):
        
        self.model = self.load_model(model_path)
        self.words, self.classes, self.intents = self.load_resources(
            words_path, class_path, intents_path)
        self.lemmatizer = WordNetLemmatizer()
        self.score_threshold = score_threshold

    @staticmethod
    def load_model(model_path):
        """
        Loads the Keras model.

        Parameters
        ----------
            model_path: str
                The path to the model.

        Returns
        -------
            model: Keras model
                The loaded model.

        Raises
        ------
            ValueError
                Raised if the model_path does not exist.
        """
        if os.path.exists(model_path):
            return load_model(model_path)
        else:
            raise ValueError(
                "The given path to the model {} does not exist.".format(
                model_path
                ))

    @staticmethod
    def load_resources(words_path, class_path, intents_path):
        """
        Reads the path to the intents, classes, and words and \
            returns the information contained in a variable.

        Parameters
        ----------
            words_path: str
                The path to the words.txt.

            class_path: str
                The path to the class.txt.

            intents_path: str
                The path to the intents.json

        Returns
        -------
            words: list
                The list of words used to train.

            classes: list
                The list of categories in the training data.

            intents: dict
                The data in the JSON file used to train. 

        Raises
        ------
            ValueError
                Raised if any of the paths does not exist.
        """
        if False in [
                os.path.exists(words_path), 
                os.path.exists(class_path), 
                os.path.exists(intents_path)
            ]:
            raise ValueError(
                "Unable to find all required resources." +
                "Check if all the paths exists:\n {}\n, {}\n, {}\n".format(
                    words_path, class_path, intents_path
                ))
        else:
            intents = json.load(open(intents_path))
            with open(words_path) as fp:
                words = [word.rstrip().lower() for word in fp.readlines()]
            fp.close()
            
            with open(class_path) as fp:
                classes = [cls.rstrip() for cls in fp.readlines()]
            fp.close()
            return words, classes, intents


    def clean_up_sentence(self, sentence):
        """
        Tokenizes and lemmatizes the provided sentence string.

        Parameters
        ----------
            sentence: str
                A provided sentence to tokenize and lemmatize.

        Returns
        -------
            sentence_words: list
                This contains all the unique words in the sentence.

        Raises
        ------
            None
        """
        sentence_words = nltk.word_tokenize(sentence)
        sentence_words = [
            self.lemmatizer.lemmatize(word).lower() for word in sentence_words]
        return sentence_words

    def bag_of_words(self, sentence):
        """
        Creates a binary array of matches where one represents a matched \
            word in the provided sentence with the current bag of words \
                stored in the text file.

        Parameters
        ----------
            sentence: str
                A provided sentence to match with the current stored words.

        Returns
        -------
            bag: np.ndarray
                A binary array of 1's and 0's indicating word matches.

        Raises
        ------
            None
        """
        sentence_words = self.clean_up_sentence(sentence)
        bag = [0] * len(self.words)
        for w in sentence_words:
            if w in self.words:
                index = self.words.index(w)
                bag[index] = 1
        return np.array(bag)

    def predict_class(self, sentence):
        """
        Deploys the model to predict the category of the sentence.

        Parameters
        ----------
            sentence: str
                The sentence to categorize.

        Returns
        -------
            return_list: list
                A list of dictionaries of possible entries provided the
                scores are greater than the set score threshold. 

        Raises
        ------
            None
        """
        bow = self.bag_of_words(sentence)
        all = self.model.predict(np.array([bow]))
        res = all[0]
        results = [
            [i, r] for i, r in enumerate(res) if r > self.score_threshold]
        results.sort(key=lambda x:x[1], reverse=True)
        return_list = list()
        for r in results:
            return_list.append({"intent": self.classes[r[0]], "probability": str(r[1])})
        return return_list

    def get_response(self, intents_list):
        """
        Gets a random response from a category described in the intents.json

        Parameters
        ----------
            intents_list: list
                A list of dictionaries of possible entries provided the
                scores are greater than the set score threshold. 

        Returns
        -------
            results: str
                The resulting random response from the intents.json 
                under the predicted category.

        Raises
        ------
            None
        """
        tag = intents_list[0]["intent"]
        list_of_intents = self.intents["intents"]
        for i in list_of_intents:
            if i["tag"] == tag:
                result = random.choice(i["responses"])
                break
        return result


if __name__ == '__main__':

    parser = argparse.ArgumentParser(
        description=("OswelNLP Model Deployment"),
        formatter_class=argparse.RawTextHelpFormatter
    )
    parser.add_argument('-m', '--model',
                        help=("The path to the Keras NLP model to deploy"),
                        type=str,
                        default=MODEL_PATH
                        )
    parser.add_argument('-w', '--words',
                        help=("The path to the words.txt"),
                        type=str,
                        default=WORDS_PATH
                        )
    parser.add_argument('-c', '--classes',
                        help=("The path to the classes.txt"),
                        type=str,
                        default=CLASS_PATH
                        )
    parser.add_argument('-i', '--intents',
                        help=("The path to the intents.json"),
                        type=str,
                        default=INTENTS_PATH
                        )
    parser.add_argument('-s', '--score',
                        help=("The score threshold to consider predictions"),
                        type=float,
                        default=0.25
                        )
    args = parser.parse_args()

    deploy = DeployOswelNLP(
        model_path=args.model,
        words_path=args.words,
        class_path=args.classes,
        intents_path=args.intents,
        score_threshold=args.score
    )

    while True:
        message = input("Sentence: ")
        ints = deploy.predict_class(message)
        res = deploy.get_response(ints)
        print(f"Response: {res}")
