# jgenerators
Template-based Java generators.

![Imgur](https://i.imgur.com/bz8wnNF.gif)

## Usage
- Download the latest release [here](https://github.com/AndanteDevs/jGenerators/releases/latest)
- Extract this ZIP so that you have a folder with simply `src` and `jgenerators.jar`
- Open a command line inside that folder
- Enter `java -jar "jgenerators.jar"` - Java 8 must be installed!
- Follow the instructions given

## Generator Types
### `template`
- Templates are read from `src/resources/templates`
- Each folder inside of the `templates` folder counts as a template
- Each template folder should have a `definitions.properties` file
    - This is where generation variables can be defined, instructions can be found in the example template
## `file with contents`
- Asks for 2 definitions, the output path and the file contents (supports `\n`)
- Outputs the file contents at the specified output path
