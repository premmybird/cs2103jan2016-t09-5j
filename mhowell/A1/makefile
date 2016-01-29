
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
	java TextBuddy memory.txt < test_input1.txt > actual_output1.txt
	java TextBuddy memory.txt < test_input2.txt > actual_output2.txt

	DIFF1="$(diff actual_output1.txt expected_output1.txt)"
	DIFF2="$(diff actual_output2.txt expected_output2.txt)"

	$(echo "difference of actual1 and expected1:\n")
	$(echo DIFF1)

	$(echo "difference of actual2 and expected2:\n")
	$(echo DIFF2)


