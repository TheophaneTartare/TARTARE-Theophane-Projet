doc:
	javadoc -sourcepath src -subpackages zombicide -d docs

cls:
	javac -sourcepath src src/zombicide/*.java -d classes

zombicide.jar: cls
	jar cvfe jar/zombicide.jar zombicide.Livrable4 -C classes .

zombicide-interactive.jar: cls
	jar cvfe jar/zombicide-interactive.jar zombicide.LivrableFinal2 -C classes .
clean:
	rm -rf docs/* classes/*

