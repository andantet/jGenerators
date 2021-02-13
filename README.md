# jgenerators
Template-based Java generators.

## Usage
- Download the latest release [here](https://github.com/AndanteDevs/jGenerators/releases/latest)
- Extract this ZIP so that you have a folder with simply `assets` and `jGenerators-VERSION.jar`
- Open a command line inside that folder
- Enter `java -jar "jGenerators-VERSION.jar"` - Java 8 or higher must be installed!
- Follow the instructions given

## How do templates work?
- Templates are read from `RUNFOLDER/assets/templates/`
- Each folder inside the `templates` folder counts as a template
- Each template folder should have a `definitions.json` file
    - This is where generation variables can be defined
    - [Example](https://github.com/andantedevs/jGenerators/blob/master/src/main/resources/assets/templates/colors/definitions.json)
