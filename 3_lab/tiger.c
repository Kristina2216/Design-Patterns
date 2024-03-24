#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>

typedef char const *(*PTRFUN)();
struct Tiger
{
   PTRFUN *vtable;
   char *name;
};

char const *tigerGreet(void)
{
   return "Rawr!";
}
char const *tigerMenu(void)
{
   return "meso";
}

char const *tigerName(void *animal)
{
   return ((struct Tiger *)(animal))->name;
}

PTRFUN tigerFunctions[3] = {
    (PTRFUN)tigerName,
    (PTRFUN)tigerGreet,
    (PTRFUN)tigerMenu};

void *create(char *name)
{
   struct Tiger *tiger = (struct Tiger *)malloc(sizeof(struct Tiger));
   tiger->name = name;
   tiger->vtable = tigerFunctions;
   return tiger;
}
