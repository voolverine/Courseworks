all: clean main parser checker link

main:
	-rm main.o
	g++ main.cpp -pthread -std=c++11 -c

parser:
	-rm parser.o
	g++ parser.cpp -std=c++11 -c

checker:
	-rm checker.o
	g++ checker.cpp -std=c++11 -c

link:
	g++ main.o parser.o checker.o ../../hashlib/hash.o -pthread -std=c++11 -o ../build/main

clean:
	-rm -rf ../build/*
