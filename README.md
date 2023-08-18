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

To train the model, an intents.json file is required under `/OswelM/oswel-mark-one/src/main/resources` which has the format:

```
{
    "intents" [
        {
            "tag": String Category,
            "patterns": ["...", "...", ...],
            "responses": ["...", "...", ...]
        },
        {
            "tag": String Category,
            "patterns": ["...", "...", ...],
            "responses": ["...", "...", ...]
        },
        { ... },
        ...
    ]
}
```

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

    *Note: There are more parameters to specify in the Python application such as setting the paths to the model and resources. Attempt to change this in your own risk because the Java application will rely on the default paths set.*

    The following command lists all the known parameters:

    ```shell
    python -m src.main.python.project.oswel --help
    ```

* **Train the OswelM Face Detection Keras Model** 

    *Coming soon*

# Java Application
*The following steps are guidelines to deploy the trained Keras models and uses external APIs to apply the concept of OswelM through the use of the Java language.*

## Step 1: Install Java SDK 11.

For linux development, ensure latest packages are available by running the commands below.

```shell
sudo apt update
sudo apt upgrade
```

The following guidelines shows how to install [Java 11](https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A).

## Step 2: Ensure resources are present.

A directory should be present under `/OswelM/oswel-mark-one/src/main/resources` which contains the following files:

* *classes.txt*         : This contains all the tags/categories in the intents.json which is used during deployment to map the score index for the specific tag. 
* *words.txt*           : This contains all the words used to train the Oswel NLP model, but also used during deployment for the lamentization process.
* *intents.json*        : This is used for training the Oswel NLP model, but also used during deployment for the responses. 
* *settings.json*       : Specify the endpoints and the current location to be used for the application.
* *oswel.lic*           : License file containing the API keys for specific endpoints.
* *en-ner-date.bin*     : An NLP model used to detect dates and times in a sentence.
* *en-ner-location.bin* : An NLP model used to detect locations in a sentence. 
* *en-pos-maxent.bin*   : An NLP model used to classify a given sentence into verbs, nouns, pronouns, etc. 
* *oswel.h5*            : The NLP model trained in the python application.

The files *classes.txt* and *words.txt* are automatically generated when training the model. However, intents.json needs to be created by the user which following the format described in the section *Python Training and Development*.

The file *settings.json* has the format:

```
{
    "endpoints": {
        "currentEvents": Link to a News API endpoint,
        "wikipedia": Link to a wikipedia API endpoint,
        "weather": Link to a weather API endpoint,
        "ChatGPT": Link to a large text generative transformer API endpoint
    },
    "resources": {
        "oswelNLPModel": "oswel.h5",
        "wordsFile": "words.txt",
        "classesFile": "classes.txt",
        "intentsFile": "intents.json",
        "locationNER": "en-ner-location.bin",
        "posNER": "en-pos-maxent.bin"
    },
    "cityLocation": "Calgary"
}
```

*Note please retain the same settings set in the repository as those specific endpoint links are currently supported.*

The file *oswel.lic* has the following format:

```
{
    "googlespeech": "API Key for speech recognition",
    "visualcrossing": "API key for weather information",
    "newsapi": "API key for news updates",
    "openai": "Not supported yet",
    "deepai": "API key for DeepAI text generation",
    "palm": "Not supported yet"
}
```

*Note all NLP models except for oswel.h5 can be found and downloaded from this [link](https://opennlp.sourceforge.net/models-1.5/). Examples for deploying the models is described in this [article](https://medium.com/@ankitagrahari.rkgit/find-names-location-or-time-in-given-string-named-entity-recognition-apache-opennlp-79ff4b30edc6).*

## Step 3: Building the Maven Project.

Once the repository is cloned, clean the workspace following the instructions below.

* Cmd + Shift + P (Ctrl + Shift + P on Windows) to show the command palette.
* Choose "Java: Clean the Java language server workspace"
* Restart and Delete

Once the workspace has been cleaned, compile the Maven project using one of the commands below:

The following command is for default system architecture such as x86_64 processors.
```shell
mvn clean -f "path to the pom.xml" compile assembly:single
```

To run the JAR file in a Raspberry PI 4 requires a linux operating system on an ARM64 processor which can be specified in the command line below.
```shell
mvn clean -Djavacpp.platform=linux-arm64 -f "path to the pom.xml" compile assembly:single
```

Depending on the system architecture, the JAR file can be run using the command below:
```shell
java -jar oswel-mark-one-1.0-jar-with-dependencies.jar   
```

## License

Copyright ©2023 John Santos. All Rights Preserved.
