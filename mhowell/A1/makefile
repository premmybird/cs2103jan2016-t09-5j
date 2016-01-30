
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
			$(JC) $(JFLAGS) $*.java

CLASSES = TextBuddy.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	$(RM) actual*
	$(RM) memory.txt

test:
	java TextBuddy memory.txt < testinput.txt > actualoutput1.txt
	java TextBuddy memory.txt < testinput2.txt > actualoutput2.txt

	DIFF1="$(diff actualoutput1.txt expected.txt)"
	DIFF2="$(diff actualoutput2.txt expected2.txt)"

	$(echo "difference of actual1 and expected1:\n")
	$(echo DIFF1)

	$(echo "difference of actual2 and expected2:\n")
	$(echo DIFF2)


