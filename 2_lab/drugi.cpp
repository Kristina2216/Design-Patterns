#include <stdio.h>
#include <cstring>
#include <iostream>
using namespace std;

template <typename Iterator, typename Predicate>
Iterator mymax(Iterator first, Iterator last, Predicate pred)
{
   Iterator max = first;
   for (Iterator iter = first; iter != last; iter++)
   {
      if (pred(max, iter) != 1)
         max = iter;
   }
   return max;
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
   char f = *(char *)first;
   char s = *(char *)second;
   if (f > s)
      return 1;
   return 0;
}
int gt_str(const void *first, const void *second)
{
   const char *f = *((const char **)first);
   const char *s = *((const char **)second);
   if (strcmp(f, s) > 0)
      return 1;
   return 0;
}

int arr_int[] = {1, 3, 5, 7, 4, 6, 9, 2, 0};
int main()
{
   const int *maxint = mymax(&arr_int[0],
                             &arr_int[sizeof(arr_int) / sizeof(*arr_int)], gt_int);
   cout << *maxint << "\n";
   getchar();
}