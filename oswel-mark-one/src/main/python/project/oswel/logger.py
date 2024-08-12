# Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>. All Rights Reserved. 
#
# Unauthorized copying of this file, via any medium is strictly prohibited.
# Proprietary and confidential.
#
# This source code is provided solely for runtime interpretation by Python.
# Modifying or copying source code is explicitly forbidden. 

def logger(message: str, code: str=''):
    """
    Outputs messages on the terminal when called.
        Type of message can be an error, a warning, \
            an info, a success, or no label.
    """
    if code.upper() == 'ERROR':
        print(f'\t - ❌ [ERROR]: {message}')
        exit(1)
    elif code.upper() == 'WARNING':
        print(f'\t - ⚠️ [WARNING]: {message}')
    elif code.upper() == 'INFO':
        print(f'\t - ℹ️ [INFO]: {message}')
    elif code.upper() == 'SUCCESS':
        print(f'\t - ✅ [SUCCESS]: {message}')
    else:
        print(f'\t - {message}')