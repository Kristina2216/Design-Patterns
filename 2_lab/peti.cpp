#include <stdio.h>
#include <cstring>
#include <iostream>
#include <cstdlib>
#include <time.h>
#include <fstream>
#include <list>
#include <typeinfo>
using namespace std;

class AbstractSource
{
public:
   virtual ~AbstractSource() {}
   virtual int loadNumber() {}
};

class ConcreteSource : public AbstractSource
{
public:
   int loadNumber()
   {
      return -1;
   }
};
class DecoratorSource : public AbstractSource
{
protected:
   AbstractSource *source_;

public:
   DecoratorSource(AbstractSource *source) : source_(source) {}
   int loadNumber()
   {
      return source_->loadNumber();
   }
};

class TipkovnickiIzvor : public DecoratorSource
{
public:
   TipkovnickiIzvor(AbstractSource *source = nullptr) : DecoratorSource(source) {}
   ~TipkovnickiIzvor()
   {
      delete this->source_;
   }

   int loadNumber() override
   {
      int number;
      cin >> number;
      return number;
   }
};

class DatotecniIzvor : public DecoratorSource
{
private:
   string path_;
   int lastIndex;

public:
   DatotecniIzvor(string path, AbstractSource *source = nullptr) : DecoratorSource(source), path_(path), lastIndex(0) {}
   ~DatotecniIzvor()
   {
      delete this->source_;
   }
   int loadNumber() override
   {
      ifstream inFile(path_);
      char singleCharacter;
      for (int i = 0; i < lastIndex; i++)
      {
         inFile.get(singleCharacter);
         //cout << "\nPetlja:" << singleCharacter << ", " << lastIndex << "\n";
      }
      inFile.get(singleCharacter);
      lastIndex++;
      inFile.close();
      if (inFile.eof())
      {
         return -1;
      }
      else
         return (int)singleCharacter - 48;
   }
};

class Observer
{

public:
   virtual string path() {}
   virtual ~Observer(){};
   virtual void Update(list<int> numbers) {}
   virtual void print() { cout << "bla"; }
};

class ObserverWriteFile : public Observer
{
private:
   string path_;

public:
   string path() override
   {
      return path_;
   }
   void print() override { cout << "Zapis u file." << path_ << "\n"; }
   ObserverWriteFile(string path) : path_(path)
   {
   }

   void Update(list<int> numbers) override
   {
      ofstream outFile(path_);
      auto l_iterator = numbers.begin();
      for (int i = 0; i < numbers.size(); i++)
      {
         outFile << *l_iterator << " ";
         l_iterator++;
      }
      time_t now = time(0);
      char *dt = ctime(&now);
      outFile << dt;
   }
};

class ObserverSum : public Observer
{
private:
   string path_;

public:
   string path() override
   {
      return path_;
   }
   void print() override { cout << "Ispis sume." << path_ << "\n"; }
   ObserverSum() : path_("sum") {}
   void Update(list<int> numbers) override
   {
      auto l_iterator = numbers.begin();
      int sum = 0;
      for (int i = 0; i < numbers.size(); i++)
      {
         sum += *l_iterator;
         l_iterator++;
      }
      cout << "Sum: " << sum << "\n";
   }
};

class ObserverAverage : public Observer
{
private:
   string path_;

public:
   string path() override
   {
      return path_;
   }
   void print() override { cout << "Ispis sume." << path_ << "\n"; }
   ObserverAverage() : path_("average") {}
   void Update(list<int> numbers) override
   {
      auto l_iterator = numbers.begin();
      int sum = 0;
      for (int i = 0; i < numbers.size(); i++)
      {
         sum += *l_iterator;
         l_iterator++;
      }
      cout << "Average: " << (float)sum / numbers.size() << "\n";
   }
};
class ObserverMedian : public Observer
{
private:
   string path_;

public:
   string path() override
   {
      return path_;
   }
   void print() override { cout << "Ispis sume." << path_ << "\n"; }
   ObserverMedian() : path_("median") {}
   void Update(list<int> numbers) override
   {
      numbers.sort();
      float median;
      if (numbers.size() % 2 == 0)
      {
         auto l_iterator = numbers.begin();
         int sum = 0;
         for (int i = 0; i < numbers.size(); i++)
         {
            sum += *l_iterator;
            l_iterator++;
         }
         median = (float)sum / numbers.size();
      }
      else
      {
         int z = numbers.size() / 2 + 1;
         auto l_iterator = numbers.begin();
         for (int i = 0; i < z - 1; i++)
         {
            l_iterator++;
         }
         median = *l_iterator;
      }
      cout << "Median: " << median << "\n";
   }
};

class SlijedBrojeva
{
private:
   AbstractSource *source_;
   list<int> numbers;
   Observer **observers;
   int N = 0;

public:
   SlijedBrojeva(AbstractSource *source) : source_(source), observers((Observer **)malloc(sizeof(Observer *))) {}
   void kreni()
   {
      int count = 1;
      double time_counter = 0;
      clock_t this_time;
      clock_t last_time;
      int number = 2;
      int first = 0;
      while (number >= 0)
      {
         int check = 0;
         time_counter = 0;
         first++;
         if (first != 1)
         {
            numbers.push_back(number);
            notifyListeners();
         }
         number = source_->loadNumber();
         last_time = clock();
         while (check = 0)
         {
            this_time = clock();
            time_counter += (double)(this_time - last_time);
            last_time = this_time;
            if (time_counter > (double)(CLOCKS_PER_SEC))
            {
               check = 1;
            }
         }
      }
   }
   void dodajPromatraca(Observer *observer)
   {
      observers = (Observer **)realloc(observers, sizeof(Observer *) * (N + 1));
      observers[N] = observer;
      N = N + 1;
   }

   void makniPromatraca(Observer *observer)
   {

      for (int i = 0; i < N; i++)
      {
         if ((*observer).path().compare((*(observers[i])).path()) == 0)
         {
            for (int j = i; j < N - 1; j++)
            {
               observers[j] = observers[j + 1];
            }
         }
      }
      N = N - 1;
   }
   void notifyListeners()
   {
      for (int i = 0; i < N; i++)
      {
         (*(observers[i])).Update(numbers);
      }
   }
   void printObservers()
   {
      for (int i = 0; i < N; i++)
      {
         (*(observers[i])).print();
      }
   }

   void printNumbers()
   {
      auto l_iterator = numbers.begin();
      for (int i = 0; i < numbers.size(); i++)
      {
         cout << *l_iterator << " ";
         l_iterator++;
      }
   }
};

int main()
{
   SlijedBrojeva *slijed = new SlijedBrojeva(new DatotecniIzvor("brojevi.txt"));
   Observer *obs = new ObserverWriteFile("izlaz.txt");
   Observer *obs1 = new ObserverSum();
   Observer *obs3 = new ObserverMedian();
   Observer *obs2 = new ObserverAverage();
   slijed->dodajPromatraca(obs);
   slijed->dodajPromatraca(obs1);
   slijed->dodajPromatraca(obs2);
   slijed->dodajPromatraca(obs3);
   slijed->kreni();
   getchar();
}