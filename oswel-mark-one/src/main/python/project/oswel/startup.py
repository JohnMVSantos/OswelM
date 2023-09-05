
from src.main.python.project.oswel.nlp.settings import MODEL_PATH, \
    CLASS_PATH, WORDS_PATH, INTENTS_PATH
from src.main.python.project.oswel.nlp.deploy import DeployOswelNLP
from src.main.python.project.oswel.logger import logger
import speech_recognition as sr
from pygame import mixer
import subprocess
import argparse
import edge_tts
import asyncio
import time
import os

class StartApp:
    """
    This class is used to start the main Oswell application
    which controls the run of the Java application which handles
    most of Oswel's functionalities.
    This python class essentially provides stability for an endless 
    recognition process.

    Parameters
    ----------
        deployOswel: DeployOswel
            This object handles loading the keras NLP model make
            predictions based on the user's input.
    """
    def __init__(
            self, 
            deployOswel
        ):
        self.deployOswel = deployOswel

    @staticmethod
    def recognize():
        """
        This method recognizes the user's prompt using the microphone. 

        Returns
        -------
            user_input: str, None
                The recognized speech from the user. Otherwise it returns
                None if the operation fails or if the speech was 
                unintelligible.
        """
        recognizer = sr.Recognizer()
        # Inside Parenthesis, option: specify which microphone to use.
        mic = sr.Microphone()

        with mic as source:
            logger("Listening...", code="INFO")
            recognizer.adjust_for_ambient_noise(source, duration=0.5)
            recognizer.pause_threshold = 0.5
            audio = recognizer.listen(source)

        try:
            logger("Recognizing...", code="INFO")
            user_input = recognizer.recognize_google(audio)
            logger(f"User said: {user_input}", code="INFO")
        except sr.UnknownValueError:
            logger("Sorry, I did not quite get that", code="WARNING")
            return None
        except sr.RequestError:
            logger(
                "Speech recognition operation failed. " + 
                "Please check your internet connection.", code="WARNING")
            return None
        return user_input
    
    def interpret(self, message):
        """
        This method interprets the user's message and returns an appropriate 
        response.

        Parameters
        ----------
            message: str
                The user's message.

        Returns
        -------
            response: list
                The model's response based on the user's message category.
                [{'intent': 'category', 'probability': float}]
        """
        return self.deployOswel.predict_class(message)
    
    def process_intent(self, ints):
        """
        This method returns an appropriate response based on 
        the user's response.

        Parameters
        ----------
            ints: list
                The model's response based on the user's message category.
                [{'intent': 'category', 'probability': float}]

        Returns
        -------
            response: str
                The response to speak.
        """
        tag = ints[0]["intent"]
        if tag.lower() in ["status", "initialization"]:
            response = self.deployOswel.get_response_by_tag("initialization")
        elif tag.lower() in ["greetings", "feeling"]:
            response = "I am currently not uploaded. " + \
                        self.deployOswel.get_response_by_tag("initialization")
        elif tag.lower == "departure":
            response = self.deployOswel.get_response_by_tag(tag)
        else:
            return None
        logger(f"Oswell says: {response}")
        return response

    def speak(self, file):
        """
        Plays the MP3 file which contains the oswel response.

        Parameters
        ----------
            file: str
                The path to the MP3 file.
        """
        mixer.init()
        mixer.music.load(file)
        mixer.music.play()
        while mixer.music.get_busy():  # Wait for music to finish playing.
            time.sleep(1)
        mixer.music.unload()
        os.remove(file)

    @staticmethod
    async def amain(message, voice, file):
        """Main function"""
        communicate = edge_tts.Communicate(message, voice)
        await communicate.save(file)

    @staticmethod
    def initialize():
        """
        This method runs the Java application using subprocess.
        """
        with subprocess.Popen(
            ['java', '-jar', '.\\target\\oswel-mark-one-1.0.jar']) as p: 
            p.wait()

    def start_process(self, voice, file):
        """
        This method starts the app process which essentially 
        recognizes the user's voice and runs the Java application 
        based on the user's intention.

        Parameters
        ----------
            voice: str
                The voice to use to speak the intention.

            file: str
                The path to save the MP3 file that saves the voice.
        """
        loop = asyncio.get_event_loop_policy().get_event_loop()
        try:
            while True:
                user_input = self.recognize()
                if user_input is None:
                    message = "Sorry, I was not able to understand that."
                    loop.run_until_complete(self.amain(message, voice, file))
                    self.speak(file)
                else:
                    ints = self.interpret(user_input)
                    message = self.process_intent(ints)
                    if isinstance(message, str):
                        loop.run_until_complete(self.amain(message, voice, file))
                        self.speak(file)    
                        tag = ints[0]["intent"]
                        if tag.lower() in [
                            "status", "initialization", "greetings", "feeling"]:
                            self.initialize() 
                    else:
                        continue
        finally:
            loop.close()

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
    parser.add_argument('-v', '--voice', 
                        help=("The voice to use to speak."),
                        type=str,
                        default="en-GB-RyanNeural"
                        )
    parser.add_argument('-f', '--file_path',
                        help=("The path to save the temporary MP3 file."),
                        type=str,
                        default="oswelAudio.mp3"
                        )
    args = parser.parse_args()

    deploy = DeployOswelNLP(
        model_path=args.model,
        words_path=args.words,
        class_path=args.classes,
        intents_path=args.intents,
        score_threshold=args.score
    )
    start = StartApp(deploy)
    start.start_process(voice=args.voice, file=args.file_path)