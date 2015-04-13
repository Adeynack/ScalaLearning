def sum(f: Int => Int)(a: Int, b: Int): Int = {
  def loop(x: Int, acc: Int): Int = if (x > b) acc else loop(x + 1, acc + f(x))
  loop(a, 0)
}

sum(x => x)(1, 2)
sum(x => x)(20, 30)
sum(x => -x)(1, 4)
sum(x => x * x * x)(5, 7)
sum(x => x * x)(3, 5) // = 50
sum(x => x)(0, 1000000)
def sum2(f: Int => Int)(a: Int, b: Int): Int = {
  if (a > b) 0 else f(a) + sum2(f)(a + 1, b)
}

sum2(x => x)(1, 2)
sum2(x => x)(20, 30)
sum2(x => -x)(1, 4)
sum2(x => x * x * x)(5, 7)
sum2(x => x * x)(3, 5) // = 50
//sum2(x => x)(0, 10000) <---- stack overflow. CQFD: Using tail-recursion is better, even if more verbal.


def product(f: Int => Int)(a: Int, b: Int): Int = {
  def loop(x: Int, acc: Int): Int = if (x > b) acc else loop(x + 1, acc * f(x))
  loop(a, 1)
}

product(x => x)(1, 3) // = 6
product(x => x + 2)(1, 3) // = 60
def fact(n: Int): Int = product(x => x)(1, n)
fact(1)
fact(2)
fact(3)
def generalised(unitValue: BigInt, comb: (BigInt, BigInt) => BigInt)(f: BigInt => BigInt)(a: BigInt, b: BigInt): BigInt = {
  def loop(x: BigInt, acc: BigInt): BigInt = if (x > b) acc else loop(x + 1, comb(acc, f(x)))
  loop(a, unitValue)
}
def sumGen = generalised(0, _ + _) _

def productGen = generalised(1, _ * _) _

generalised(1, _ * _)(x => x)(1, 3)
productGen(x => x)(1, 3)
generalised(0, _ + _)(x => x)(1, 2)
sumGen(x => x)(1, 2)
generalised(0, _ + _)(x => x)(20, 30)
sumGen(x => x)(20, 30)
sumGen(x => x)(0, 1000000)
productGen(x => x)(1, 20)
def mapReduce(f: Int => Int, combine: (Int, Int) => Int, zero: Int)(a: Int, b: Int): Int = {
  def loop(x: Int, acc: Int): Int = if (x > b) acc else loop(x + 1, combine(acc, f(x)))
  loop(a, zero)
}
def sumMR: ((Int) => Int) => (Int, Int) => Int = mapReduce(_, _ + _, 0)

def productMR: ((Int) => Int) => (Int, Int) => Int = mapReduce(_, _ * _, 1)

productMR(x => x)(1, 3)
sumMR(x => x)(1, 2)
sumMR(x => x)(20, 30)
productMR(x => x)(1, 20)
