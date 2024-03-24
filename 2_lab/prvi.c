#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

const void *mymax(const void *base, size_t nmemb, size_t size, int (*compar)(const void *, const void *))
{
   const void *ret = base;
   for (int i = 1; i < nmemb; i++)
   {
      if (compar(base, ret) == 1)
         ret = base;
      base = base + size;
   }
   return ret;
}

int gt_int(const void *first, const void *second)
{
   int f = *((int *)first);
   int s = *((int *)second);
   if (f > s)
      return 1;
   return 0;
}
int gt_char(const void *first, const void *second)
{
   char *f = *(char *)first;
   char *s = *(char *)second;
   if (f > s)
      return 1;
   return 0;
}
int gt_str(const void *first, const void *second)
{
   char *f = *((const char **)first);
   char *s = *((const char **)second);
   if (strcmp(f, s) > 0)
      return 1;
   return 0;
}
int main()
{
   int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
   char arr_char[] = "Suncana strana ulice";
   const char *arr_str[] = {
       "Gle", "malu", "vocku", "poslije", "kise",
       "Puna", "je", "kapi", "pa", "ih", "njise"};
   printf("Najveci broj je: %d\n", *((int *)(mymax(arr_int, 9, sizeof(int), &gt_int))));
   printf("Najveci string je: %s\n", *((char **)(mymax(arr_str, 11, sizeof(char *), &gt_str))));
   printf("Najveci char je: %c", *(char *)(mymax(arr_char, 21, sizeof(char), &gt_char)));
   getchar();
}
