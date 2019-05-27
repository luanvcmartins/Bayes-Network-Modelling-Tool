# Bayes Network Modelling Tool

This is a Java application capable of modeling and generating Bayes Networks and analysing accuracy, predict values and execute sampling with **prior sampling**, **rejection sampling** and **likelihood weight sampling**.

The application is capable of loading a .csv file, identify the attributes and its' possible values, and provide the user a set of tools so he may model a bayes network with a graphical interface.

This was "over engineered" project for the 2018 AI class. I kind of went too far on this one, the graphical modelling tool wasn't required, neither some of the sampling techniques and the "manual" prediction funcionallity. So no guarantees of anything.

## Modeling a network

After loading the .csv file, the user can drag the attributes to the desired location and model the network by drawing the relationships, which can be done by clicking in the "OUT" button of the parent attribute and dragging the arrow to the "IN" button of the child attribute.

<img src="https://i.imgur.com/isT2Usf.gif">

After the network is done, the user can save or load it to work on it later.

## Checking the network accuracy

After modelling the network, the user can check its' accuracy using the "check network accuracy" options avaliable on the "Network" tab. Just select the attribute on the "class" combo box and click on the "Run tests button". A file selection window will appear where the user can select a test ".csv" file.

<img src="https://i.imgur.com/2rsfA58.png">

After the testing is done, the accuracy as well as the number of correct and incorrect predictions will be provided.

## Predictions

The user can use the network to execute predictions. In the "Predict (manual)" tab, after selecting the desired values for each attribute using the list of attributes and the "value for" panel, select the attribute to predict and click on the "PREDICT" button.

<img src="https://i.imgur.com/se3dz9Q.png">

## Inference

The application allow inferences to be executed using **prior sampling**, **rejection sampling** and **likelihood weight sampling**. Just select the desired technic, the sampling size, as well as the desired values for each attribute you want and click on the "generate sample" button. A report will be generated once the process is completed.
