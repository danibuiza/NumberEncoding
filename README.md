NumberEncoding
==============
==============

This is an application that encodes phone numbers using a list of valid words.

The encoding uses following mapping rules:

E | J N Q | R W X | D S Y | F T | A M | C I V | B K U | L O P | G H Z
e | j n q | r w x | d s y | f t | a m | c i v | b k u | l o p | g h z
0 |   1   |   2   |   3   |  4  |  5  |   6   |   7   |   8   |   9

Following requirements apply:

-The list of phones can be infinite and should not be stored in memory.

-The list of words has a limit of 75000 and can be stored in memory

-Each word and phone has a maximun lenght of 50 chars

-Phones can be encoded using only whole words combined

-If it is not possible to encode a phone using a combination of words a number of the phone can be picked and passed to the end or the beggining of one of the parts of the combination

==============
Implementation details
==============
- Since there are several encodings for each phone number possible and only one decoding for a given word in the dictionary, I took the decission of revert the process:

- I created a dictionary of words with its related decoded number

- For each phone I filtered the words which encoded number are parts of the phone, the others are irrelevant

- For each phone I checked all the possible matches using all the filtered words available using recursion

- I executed the program using a pool of Threads (using java.util.concurrent.ExecutorService), 10 seems to be a good number from the results I got

- The recursion process may be quite complicated to explain, it is better to check the code in the class com.danibuiza.for360t.numberencoding.NumberEncoder

- In case that a piece of the phone is needed to encode it, this entry will be inserted to a list of possible conflicts that has to be processed at the end of the matching process for this phone. It is not possible to know for a given phone and dictionary item if it is a valid result or not without checking all the other potential results

==============
Dependencies
==============
- JRE update 8: Since I am using Lambdas, Streams and other Java 8 features, this is needed.

- Junit should be available

- Maven should be installed in your machine and should point to the proper JRE 8

==============
Run
==============
-The project can be built and executed using maven:

Build and compile: mvn package (in the directory where the pom.xml is located)

Execute: java -cp target/numberEncoding-1.0.jar com.danibuiza.for360t.numberencoding.NumberEncodingMain

==============
Tests
==============

-Unit tests are located under the tests directory, to execute them using Junit you need to:

- Regression tests are located under regressionTests, to execute them you need to:
