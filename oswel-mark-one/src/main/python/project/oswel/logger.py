
def logger(message, code=''):
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