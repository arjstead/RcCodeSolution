To compile, run these commands from src/

javac -classpath ../lib/gson-2.6.2.jar Vehicle.java
javac -classpath ../lib/gson-2.6.2.jar:. Solution.java

Having compiled the code run it from the same directory using:

java -classpath ../lib/gson-2.6.2.jar:. Solution

Alternatively run it straight from the bin folder using:

java -classpath ../lib/gson-2.6.2.jar:. Solution

Please note that £ has been replaced with the more traditional L to avoid encoding errors.



Thanks!

Alex