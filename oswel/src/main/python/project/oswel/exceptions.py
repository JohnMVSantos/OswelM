# Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>. All Rights Reserved. 
#
# Unauthorized copying of this file, via any medium is strictly prohibited.
# Proprietary and confidential.
#
# This source code is provided solely for runtime interpretation by Python.
# Modifying or copying source code is explicitly forbidden. 

import sys

class UnrecognizedTaskException(Exception):
    """
    Raised when the provided task is not recognized.

    Parameters
    ----------
        task: str
            The provided task.
    """
    def __init__(self, task: str):
        sys.tracebacklimit=0
        super(UnrecognizedTaskException, self).__init__(
            "Unrecognized task {}. Can only accept 'train' or 'deploy'.".format(
                task
            )
        )