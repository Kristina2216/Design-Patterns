import sys
import os

def mymax(iterable, key=None):
   max_x=max_key=None
   if(key==None):
      i=0
      for x in iterable:
         if(i==0):
            max_x=x
         if(x>max_x):
            max_x=x
         i=i+1
   else:
      i=0
      for x in iterable:
         if(i==0):
            max_x=x
            max_key=key(x)
         if(key(x)>max_key):
            max_x=x
            max_key=key(x)
         i=i+1
   return max_x

maxint = mymax([1, 3, 5, 7, 4, 6, 9, 2, 0])
maxchar = mymax("Suncana strana ulice")
maxstring = mymax([
  "Gle", "malu", "vocku", "poslije", "kise",
  "Puna", "je", "kapi", "pa", "ih", "njise"])
D={'burek':8, 'buhtla':5}
students=[["Paul", "McCartney"],["George", "Harrison"], ["John", "Lennon"],["Ringo", "Star"]]
f = lambda x: 2*x+3
z= lambda x: D.get(x)
personSort= lambda x: x[1] 
print(maxint)
print(maxchar)
print(maxstring)
print(mymax([1, 3, 5, 7, 4, 6, 9, 2, 0],f))
print(mymax(D,z))
print(mymax(students,personSort))