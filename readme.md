# Robust Barrier Coverage in the IoT - Matheus Centa/Joao Rocha Oliveira

This project is executed by running the Main.java in the /bin folder with two program arguments (the order of arguments is important): 
* mode: the first argument is either "-hm" for a homogeneous lifetime network as input or "-ht" for a heterogeneous lifetimes network;
* file: the second is the input file name with the extension. The input file must be placed in the \input directory.
For example, to test the program on the first network, we execute the following line at the command line: 
'java -jar main.jar -hm sensornetwork0.doc', while on the bin directory. 

The input and output follow the following guidelines:
* input: this is a text file in the \input directory which represents an instance of the K-Barrier Coverage Problem.
         The formating is as follows (new lines are not necessary but recommended):
	 	n (integer - number of sensors)
	 	K (integer - coverage redundancy)
		X_max (real number - the right wall coordinate)
		Ymax (real number - the coordinate of the upper border of the region)
		R (real number - the individual sensor coverage radius)
		a list of all x-coordinates of the n sensors (list of n real numbers within 0 to X_max)
		a list of all y-coordinates of the n sensors (list of n real numbers within 0 to Y_max)
		c for every sensor plus a starting and ending value which will be ignored (list of n+2 integers)

* output: the output is a schedule, presented as a list of intervals and the sensor paths active during those interval e.g.
			( 2,  5): [1, 2, 6]
			          [3, 4, 7]
	  means that for the interval from time 2 to 5, the sensors paths 1---2---6 and 3---4---7 will be active.
	  The output also consists of the network lifetime and the processing time (not counting reading and printing).
	  
	  It is important to note that the output is printed to the console (stdout), if you want an output to a .txt on aspecific 
	  directory (for example, \results) just add the paramenter ' > ..\results\my_output.txt' to the end of the command.
   
