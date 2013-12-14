#JAVAC_FLAGS=-Xlint
JAVAC_FLAGS=
JAVA=java
JAVAC=javac $(JAVAC_FLAGS)
RM=rm

JAVA_SRC=$(shell find greek -name '*.java')
JAVA_CLASS=$(JAVA_SRC:.java=.class)

COMPILE=$(JAVAC) $<

all: $(JAVA_CLASS)

%.class: %.java
	$(COMPILE)

clean:
	find * -name '*.class' -exec $(RM) '{}' ';'

