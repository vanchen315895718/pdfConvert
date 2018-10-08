*************** pdf_convert -- Project description**************************
This project is used to transform PDF to PNG format images.
The convertpdf2jpg.url of the configuration file is the 
absolute path of PDF that needs to be transformed.

The project traverses all the PDF files in the directory according to the
configuration address and generates a PNG image file with the same name and
different suffixes in the same directory. Pages larger than 5 PDF will not 
be converted and will be printed out through a redirected log.

start command:nohup java -jar pdf_convert.jar &
Simultaneous monitoring nohup.out log
****************************************************************************