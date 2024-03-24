#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>

typedef char const* (*PTRFUN)();

//struktura Animal
typedef struct Animal{
	char *name;
	PTRFUN *functions;
} Animal;

PTRFUN functions[2]={NULL, NULL};

void animalPrintGreeting(Animal* animal){
	printf("%s pozdravlja: %s",animal->name, animal->functions[0]());
	printf("\n");
}
void animalPrintMenu(Animal* animal){
	printf("%s voli: %s",animal->name, animal->functions[1]());
	printf("\n");
}

//pozdravi i meni
char const* dogGreet(void){
  return "vau!";
}
char const* dogMenu(void){
  return "kuhanu govedinu";
}
char const* catGreet(void){
  return "mijau!";
}
char const* catMenu(void){
  return "konzerviranu tunjevinu";
}

//pas
PTRFUN dogFunctions[2] = {
	(PTRFUN)dogGreet,
	(PTRFUN)dogMenu
};

Animal* constructDog(Animal* animal){
	animal->functions = dogFunctions;
	return animal;
}

Animal* createDog(char* name){
	Animal* animal = (Animal*)malloc(sizeof(Animal));
	animal->name = name;
	return constructDog(animal);
}

//n pasa jednim pozivom funkcije malloc
void createNDogsMalloc(int n){
	Animal* animals=(Animal*)malloc(sizeof(Animal)*4);
	for (int i = 0; i < n;i++){
		constructDog(&animals[i]);
		printf("Creating %d. dog\n Animal address=%p\n", i+1, (void*)&animals[i]);
	}
}

//n pasa visestrukim pozivom funkcije
void createNDogs(int n){
	for (int i = 0; i < n;i++)
		createDog("dog");
}

//macka
PTRFUN catFunctions[2] = {
	(PTRFUN)catGreet,
	(PTRFUN)catMenu,
};

Animal* constructCat(Animal* animal){
	animal->functions = catFunctions;
	return animal;
}

Animal* createCat(char* name){
	Animal* animal = (Animal*)malloc(sizeof(Animal));
	animal->name = name;
	return constructCat(animal);
}


void testAnimals(void){
  struct Animal* p1=createDog("Hamlet");
  struct Animal* p2=createCat("Ofelija");
  struct Animal* p3=createDog("Polonije");

  animalPrintGreeting(p1);
  animalPrintGreeting(p2);
  animalPrintGreeting(p3);

  animalPrintMenu(p1);
  animalPrintMenu(p2);
  animalPrintMenu(p3);

  free(p1); free(p2); free(p3);
}

int main(int argc, char *argv[]) {
	testAnimals();

}
