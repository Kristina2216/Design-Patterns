
#include <stdio.h>
#include <stdlib.h>
#include "myfactory.h"

typedef char const *(*PTRFUN)();

typedef struct Animal
{
  PTRFUN *vtable;
  char *name;
} Animal;

PTRFUN vtable[3] = {
    NULL, NULL, NULL};

void animalPrintGreeting(struct Animal *animal)
{
  printf("%s pozdravlja: %s", animal->vtable[0](animal), animal->vtable[1]());
  printf("\n");
}
void animalPrintMenu(struct Animal *animal)
{
  printf("%s voli: %s", animal->vtable[0](animal), animal->vtable[2]());
  printf("\n");
}

int main(void)
{
  const char *arg[] = {"./parrot.dll", "./tiger.dll"};
  for (int i = 0; i < 2; ++i)
  {
    struct Animal *p = (struct Animal *)myfactory(arg[i], "Modrobradi");
    if (!p)
    {
      printf("Creation of plug-in object %s failed.\n", arg[i]);
      continue;
    }
    animalPrintGreeting(p);
    animalPrintMenu(p);
    free(p);
  }
}
