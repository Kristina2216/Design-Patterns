#include <windows.h>
#include <stdio.h>

typedef struct Animal *(*TheFunc)(char const *);

void *myfactory(char const *libname, char const *ctorarg)
{
    HMODULE module = LoadLibrary(libname);
    return ((TheFunc)GetProcAddress(module, "create"))(ctorarg);
}