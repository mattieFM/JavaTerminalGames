JAVAC=javac --class-path "./libs/*" --source-path ./src ./src/ava/Snake.java
sources := $(shell find $(SOURCEDIR) -name '*.java')
classes = $(sources:.java=.class)

all: program

program: 
	$(classes)
	jar cvf arcade.jar $(classes)

clean:
	rm -f arcade.jar

%.class : %.java
	$(JAVAC) $<
	

.PHONY: all program clean jar