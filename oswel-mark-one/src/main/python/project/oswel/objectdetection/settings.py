# Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>. All Rights Reserved. 
#
# Unauthorized copying of this file, via any medium is strictly prohibited.
# Proprietary and confidential.
#
# This source code is provided solely for runtime interpretation by Python.
# Modifying or copying source code is explicitly forbidden. 

"""
Object Detection settings for Oswel NLP Training
"""

from pathlib import Path
import os

BASE_DIR = Path(__file__).parents[4]
# Path to the Face Detection Model
FACE_PATH = os.path.join(BASE_DIR, "resources/oswelFace.h5") 