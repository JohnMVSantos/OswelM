# Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>. All Rights Reserved. 
#
# Unauthorized copying of this file, via any medium is strictly prohibited.
# Proprietary and confidential.
#
# This source code is provided solely for runtime interpretation by Python.
# Modifying or copying source code is explicitly forbidden. 

"""
NLP settings for Oswel NLP Training
"""

from pathlib import Path
import os

BASE_DIR = Path(__file__).parents[4]
# Path that stores the intents.json
INTENTS_PATH = os.path.join(BASE_DIR, "resources/intents.json") 
# Path to save the words.txt during training
WORDS_PATH = os.path.join(BASE_DIR, "resources/words.txt") 
# Path to save the class.txt during training
CLASS_PATH = os.path.join(BASE_DIR, "resources/classes.txt") 
# Path to save the model file during training
MODEL_PATH = os.path.join(BASE_DIR, "resources/oswel.h5") 