#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef double (*PTRFUN)();

//Unary function		
typedef struct Unary_Function{
	int lower_bound;
  int upper_bound;
	PTRFUN *functions;
} Unary_Function;

PTRFUN functions[2] = {NULL, NULL};


struct Unary_Function* constructUnaryFunction(int lb, int ub){
	Unary_Function* uf = (Unary_Function*)malloc(sizeof(Unary_Function));
	uf->lower_bound = lb;
	uf->upper_bound = ub;
	uf->functions=functions;
	return uf;
}

//Square
typedef struct Square{
	struct Unary_Function uf;
} Square;

double square_value_at(Unary_Function* f, double x){
	return x * x;
}

PTRFUN squareFunctions[2] = {
		(PTRFUN)square_value_at,
		(PTRFUN)square_value_at};

struct Unary_Function *constructSquareFunction(int lb, int ub)
{
	struct Square *square = (struct Square *)constructUnaryFunction(lb, ub);
	square->uf.functions= squareFunctions;
	return (struct Unary_Function*) square;
}


//Linear
typedef struct Linear{
	struct Unary_Function uf;
	double a;
	double b;
} Linear;

double linear_value_at(Unary_Function* f, double x){
	struct Linear *linear = (struct Linear *)f;
	return linear->a * x + linear->b;
}

PTRFUN linearFunctions[2] = {
		(PTRFUN)linear_value_at,
		(PTRFUN)linear_value_at};

struct Unary_Function* constructLinearFunction(int lb, int ub, int a, int b){
	struct Linear *linear = (struct Linear *)constructUnaryFunction(lb, ub);
	linear->uf.functions = linearFunctions;
	linear->a=a;
	linear->b = b;
	return (struct Unary_Function*)linear;
}


double value_at(struct Unary_Function* f, double x){
	return f->functions[0](f,x);
}

double negative_value_at(struct Unary_Function* f, double x){
	return -1*f->functions[0](f,x);
}

void tabulate(struct Unary_Function* f) {
  for(int x = f->lower_bound; x <= f->upper_bound; x++) {
    printf("f(%d)=%lf\n", x, value_at(f,x));
  }
}

bool same_functions_for_ints(struct Unary_Function *f1,struct  Unary_Function *f2, double tolerance) {
  if(f1->lower_bound != f2->lower_bound) return false;
  if(f1->upper_bound != f2->upper_bound) return false;
  for(int x = f1->lower_bound; x <= f1->upper_bound; x++) {
    double delta = f1->functions[0](f1,x) - f2->functions[0](f2,x);
    if(delta < 0) delta = -delta;
    if(delta > tolerance) return false;
  }
  return true;
  }


int main(int argc, char *argv[])
{

  Unary_Function *f1 =  constructSquareFunction(-2, 2);
  tabulate(f1);
  Unary_Function *f2 = constructLinearFunction(-2, 2, 5, -2);
  tabulate(f2);
  printf("f1==f2: %s\n", same_functions_for_ints(f1, f2, 1E-6) ? "DA" : "NE");
  printf("neg_val f2(1) = %lf\n", negative_value_at(f2, 1.0));
	free(f1);
	free(f2);
	getchar();
	return 0;



}