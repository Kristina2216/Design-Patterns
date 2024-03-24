#include <stdio.h>
#include <iostream>
using namespace std;

class B
{
public:
   virtual int __cdecl prva() = 0;
   virtual int __cdecl druga(int) = 0;
};

class D : public B
{
public:
   virtual int __cdecl prva() { return 42; }
   virtual int __cdecl druga(int x) { return prva() + x; }
};

typedef int (*PTRFUN1)();
typedef int (*PTRFUN2)(B *, int);

void function(B *ptr)
{
   PTRFUN1 *vT1 = *(PTRFUN1 **)ptr;
   PTRFUN2 *vT2 = *(PTRFUN2 **)ptr;
   cout << vT1[0]() << endl;
   cout << vT2[1](ptr, 2);
}

int main(int argc, char *argv[])
{
   B *d = new D();
   function(d);
   getchar();
   return 0;
}
