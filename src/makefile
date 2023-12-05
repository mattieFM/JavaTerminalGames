JAVAC=javac 
JFLAGS=--class-path "../libs/*"  --source-path ./
sources := $(shell find ./ -name '*.java')
classes = $(sources:.java=.class)



all: program

program: 
	$(classes)

clean:
	rm -f $(classes)
	rm manifest.txt

%.class : %.java
	@echo $$PATH
	$(JAVAC) $(JFLAGS) $<
	
jar:
	@echo "Manifest-Version: 1.0" > manifest.txt
	@echo "Main-Class: main.JavaTerminalArcade" >> manifest.txt
	@echo " " >> manifest.txt
	jar cvfm ../games.jar manifest.txt $(classes)

.PHONY: all program clean jar