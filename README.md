# OswelM
 
*Ordinary Systems of a Well Established Language Model* is the concept that a language model
should not be limited to one system of a specific functionality (Natural Language Processing), it should link multiple systems 
such as Generative Pretrained Transformers and Vision Models to gather and process information to effectively communicate to the user the appropriate response based on their demands. 


# Python Training and Deployment
*The following steps are guidelines to train and test Keras NLP or object recognition models in Python which will be deployed in the Java application.*

## Step 1: Start a Python Virtual Environment

It is recommended to start a [python virtual environment](https://www.freecodecamp.org/news/how-to-setup-virtual-environments-in-python/) to contain the installed dependencies within the project folder.

*Note this virtual environment needs to be activated at the start each time when using the application.*

## Step 2: Install the Python Dependency Requirements

To install the dependencies, run the following shell command:

```shell
pip install -r requirements.txt
```

## Step 3: Model Training.

* **Train the OswelM NLP Keras Model**

    In the first run, the following resources needs to be downloaded. Run
    the following commands...

    ```shell
    python
    import nltk
    nltk.download("wordnet", "path to the virtual environment/nltk_data")
    ```

    To train the NLP model, run the following shell commands:

    ```shell
    cd oswel-mark-one
    python -m src.main.python.project.oswel -t train
    ```

    To deploy the trained NLP model in python (primarily used for testing), 
    in the same directory, run the following command:

    ```shell
    python -m src.main.python.project.oswel -t deploy
    ```

    *Note: There are more parameters do specify in the Python application such as setting the paths to the model and resources. Attempt to change this in your own risk because the Java application will rely on the default paths set.*

    The following command lists all the known parameters:

    ```shell
    python -m src.main.python.project.oswel --help
    ```

* **Train the OswelM Face Detection Keras Model** 

    *Coming soon*

# Java Application
*The following steps are guidelines to deploy the trained Keras models and uses external APIs to apply the concept of OswelM through the use of the Java language.*

Install Java SDK 11

sudo apt update
sudo apt upgrade
sudo apt install 

Clone the Repo...
Clean the workspace 
Cmd + Shift + P (Ctrl + Shift + P on Windows) to show the command palette
Choose "Java: Clean the Java language server workspace"
Restart and Delete
Install redhat.fabric8-analytics vscode extension

Cleaning and compiling
`
& mvn clean -f "c:\Users\johns\Documents\OswelM\oswel-mark-one\pom.xml" compile assembly:single
or
mvn clean -Djavacpp.platform=linux-arm64 -f "./oswel-mark-one/pom.xml" compile assembly:single
`

Running
`
java -jar .\oswel-mark-one\target\oswel-mark-one-1.0-jar-with-dependencies.jar   
`

## Resources

## License

