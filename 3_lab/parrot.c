#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>

typedef char const *(*PTRFUN)();
struct Parrot
{
   PTRFUN *vtable;
   char const *name;
};

char const *parrotGreet(void)
{
   return "Sto mu gromova!";
}
char const *parrotMenu(void)
{
   return "brazilske orahe";
}

char const *parrotName(void *animal)
{
   return ((struct Parrot *)(animal))->name;
}

PTRFUN parrotFunctions[3] = {
    (PTRFUN)parrotName,
    (PTRFUN)parrotGreet,
    (PTRFUN)parrotMenu};

void *create(char const *name)
{
   struct Parrot *parrot = (struct Parrot *)malloc(sizeof(struct Parrot));
   parrot->name = name;
   parrot->vtable = parrotFunctions;
   return parrot;
}
