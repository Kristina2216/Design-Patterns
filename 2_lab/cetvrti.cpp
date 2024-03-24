#include <stdio.h>
#include <cstring>
#include <iostream>
#include <cstdlib>
#include <random>
#include <ctime>
using namespace std;

class AbstractGenerateNumbers
{
public:
   virtual ~AbstractGenerateNumbers() {}
   virtual int *generateNumbers(int a = 0, int b = 0, int c = 0){};
};

class GenerateNumberSteps : public AbstractGenerateNumbers
{
private:
   AbstractGenerateNumbers *generator_;

public:
   GenerateNumberSteps(AbstractGenerateNumbers *generator = nullptr) : generator_(generator) {}
   ~GenerateNumberSteps()
   {
      delete this->generator_;
   }

   int *generateNumbers(int start, int end, int step) override
   {
      int number = (end - start) / step;
      int *array = new int[number];
      for (int i = 0; i < number; i++)
      {
         array[i] = start + i * step;
      }
      return array;
   }
};

class GenerateNormalDistribution : public AbstractGenerateNumbers
{
private:
   AbstractGenerateNumbers *generator_;

public:
   GenerateNormalDistribution(AbstractGenerateNumbers *generator = nullptr) : generator_(generator) {}
   ~GenerateNormalDistribution()
   {
      delete this->generator_;
   }

   int *generateNumbers(int mean, int deviation, int number) override
   {
      int *array = new int[number];
      default_random_engine gen(time(0));
      normal_distribution<double> distribution(mean, deviation);
      for (int i = 0; i < number; i++)
      {
         array[i] = distribution(gen);
      }
      return array;
   }
};

class GenerateFibonacci : public AbstractGenerateNumbers
{
private:
   AbstractGenerateNumbers *generator_;

public:
   GenerateFibonacci(AbstractGenerateNumbers *generator = nullptr) : generator_(generator) {}
   ~GenerateFibonacci()
   {
      delete this->generator_;
   }

   int *generateNumbers(int number, int a = 0, int b = 0) override
   {
      int *array = new int[number];
      for (int i = 0; i < number; i++)
      {
         if (i == 0 || i == 1)
            array[i] = 1;
         else
         {
            array[i] = array[i - 2] + array[i - 1];
         }
      }
      return array;
   }
};

class AbstractPercentilCount
{
public:
   virtual ~AbstractPercentilCount() {}
   virtual int countPercentil(int *array, int n, int p){};
};

class PercentilCountIndex : public AbstractPercentilCount
{
private:
   AbstractPercentilCount *counter_;

public:
   PercentilCountIndex(AbstractPercentilCount *counter = nullptr) : counter_(counter) {}
   ~PercentilCountIndex()
   {
      delete this->counter_;
   }
   int countPercentil(int *array, int n, int p) override
   {
      int index = round(p * n / 100 + 0.5);
      return array[index - 1];
   }
};

class PercentilCountInterpolated : public AbstractPercentilCount
{
private:
   AbstractPercentilCount *counter_;

public:
   PercentilCountInterpolated(AbstractPercentilCount *counter = nullptr) : counter_(counter) {}
   ~PercentilCountInterpolated()
   {
      delete this->counter_;
   }
   int countPercentil(int *array, int n, int p) override
   {
      }
};

main()
{
   PercentilCountIndex counter = new PercentilCountIndex();
   cout << counter.countPercentil(new int[3]{1, 10, 50}, 3, 80) << "\n";
   GenerateNormalDistribution fib = new GenerateNormalDistribution();
   int *arr = fib.generateNumbers(2, 5, 5);
   for (int i = 0; i < 5; i++)
   {
      cout << arr[i];
   }
   getchar();
}