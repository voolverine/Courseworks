all: clean main counter link

main:
	g++ main.cpp -o ../build/main.o -std=c++11 -c

counter:
	g++ counter.cpp -o ../build/counter.o -std=c++11 -c

link:
	g++ ../build/main.o ../build/counter.o ../../hashlib/hash.o -o ../build/main -std=c++11

clean:
	rm -f *~
	rm -f ../build/*
