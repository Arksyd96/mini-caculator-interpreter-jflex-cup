lexer:
	java -cp "libs/JFlex.jar:." JFlex.Main resource/lexer.lex -d src/
	java -cp "libs/java-cup-11b.jar:." java_cup.Main -destdir src/ resource/parser.cup
	javac -cp "libs/java-cup-11b-runtime.jar:." -d cls/ src/*.java
parse:
	@echo Starting parser :
	@java -cp "cls:libs/java-cup-11b-runtime.jar:." parser
test:
	@javac -cp "cls/:libs/*:." -d cls/ src/test/ParserTest.java
	@java -cp "cls/:libs/*:." org.junit.runner.JUnitCore ParserTest