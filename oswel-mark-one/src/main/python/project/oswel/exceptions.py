
import sys

class UnrecognizedTaskException(Exception):
    """
    Raised when the provided task is not recognized.

    Parameters
    ----------
        task: str
            The provided task.
    """
    def __init__(self, task):
        sys.tracebacklimit=0
        super(UnrecognizedTaskException, self).__init__(
            "Unrecognized task {}. Can only accept 'train' or 'deploy'.".format(
                task
            )
        )