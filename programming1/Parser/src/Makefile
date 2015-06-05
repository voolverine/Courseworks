all: clean main text_parser link

link:
	g++ ../build/main.o ../build/text_parser.o -std=c++11 -o ../build/main -pthread

main:
	g++ main.cpp -c -std=c++11 -o ../build/main.o -pthread

text_parser:
	g++ text_parser.cpp -c -std=c++11 -o ../build/text_parser.o

clean:
	rm -f *.o *~ ../build/*
