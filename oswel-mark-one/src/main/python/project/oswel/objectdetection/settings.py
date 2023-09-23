"""
Object Detection settings for Oswel NLP Training
"""

from pathlib import Path
import os

BASE_DIR = Path(__file__).parents[4]
# Path to the Face Detection Model
FACE_PATH = os.path.join(BASE_DIR, "resources/oswelFace.h5") 