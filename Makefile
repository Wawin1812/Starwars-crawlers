#JAVAFX_PATH = /Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/jre/lib/ext/jfxrt.jar
JFXP = ${JAVAFX_PATH}
SOURCES = $(wildcard src/ass2/*.java src/ass2/*/*.java)

all: compile doc

compile: $(SOURCES)
	@@javac -d bin -cp .:$(JFXP) $(SOURCES)

doc: $(SOURCES)
	@@javadoc @doc_args $(SOURCES)

run: compile
	@@java -cp bin:$(JFXP) ass2.StarWarsCrawler &

clean:
	@@echo "Deleting all generated files"
	@@rm -rf bin/*
	@@rm -rf docs/*

diag:
	@@echo $(JFXP)
	@@echo $(SOURCES)
