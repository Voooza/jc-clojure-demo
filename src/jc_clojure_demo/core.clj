(ns jc-clojure-demo.core
  (:import (java.lang ArithmeticException))
  (:gen-class))

(println "Hello Java Community")

;; polish notation
;; coming from the fact that + is a function it follows same syntax asother functions  
(+ 1 2 3)
;; like max for example
(max 1 2 3)
;; you might have noticed that arguments are separated just by space, no "," required
;; but if you insist:
(max, 1, 2, 3)
;; in most cases coma is just considered a whitespace

;; Some might be wondering why is the name of a function inside bracket along with its arguments.
;; The reason has a fancy name: homoiconicity
;; It just means that functions are written in the same way as data
;; In fact, all examples above are just lists where the name of the function is the first item
;; But for what benefit? "the macro"

;; Now lets sort a vector:
(sort [3 2 1])
(into [] (sort [3 2 1]))

;; Can we also sum it?
(+ [1 2 3]) ;; throws java.lang.ClassCastException
;; Ahh clojure.core/+ only seems to support numeric arguments and vector is not a number...
;; never mind. lets creete a function that supports it
(def ++ (partial reduce +))

(++ [1 2 3])
;; awesome.
;; clojure allows nice function names :-)

;; OK I have shown you one way of defining a function above, but lets get back backto basis
(defn a-function
  "This function definition I believe is the most ususal.
  This is a function docstring.
  Above you can see the defn followed by the name.
  And below there are some arguments.
  Last evaluated expression is the return value."
  [arg0 arg1]
  arg1)

(a-function "argument" "another argument")
;; I can also print the docstring, because, well it's just data in the list
(doc a-function)


;; function overloading within a single function
;; or as clojurist would  call it: multi-arity
(defn multi-arity-function
  ([] "No args provided")
  ([a] "Single argument provided")
  ([a & rest] (str (inc (count rest)) " args provided")))

(multi-arity-function )
(multi-arity-function 1)
(multi-arity-function 0 1 2 3 4 5 6 7 8 9 )

;; What if I receive a collection as an argument and want to map some items directly to names
;; destructuring
(defn destructurer
  [[a b]]
  (str "a is " a
       " and b is " b))

(destructurer [1 2])
(destructurer [1 2 3])

(def person {:name "Oliver" :gender "male" :age "40"})

(let [{n :name g :gender a :age} person]
  (println (str n " is " a " years old " g)))



;; concurency
(defn long-running-function
  [a]
  (println "start")  ;; BTW: I can also evaluate this expression separately
  (Thread/sleep 1000) ;; see how I can call static java method easily from clojure
  (println (str "done with " a))
  a)

(long-running-function "argument")

(def values [0 1 2 3 4 5 6 7 8 9])

(map long-running-function values)
;; lets try with paralel map
(pmap long-running-function values)

;; memo
;; clojure can remember results for long running functions
;; Pays off if you have long runnning function called frequently with same args
(def lrf-memed (memoize long-running-function))

;; first invocation caches the result
;; second invocation returns imediatelly
(map lrf-memed values)


;; partial
;; we already saw an example above with defining a ++ function
;; Suppose you have a function which accepts multiple args
;; and you figure out that you are often calling it with almost all argumets same
;; altering only the last one or two
(defn concat-and-print
  [a b c d]
  (println (clojure.string/join " " [a b c d])))

(concat-and-print "a" "b" "c" "d")
(concat-and-print "Hello" "," "Java" "community")

(def afternoon-greeter (partial concat-and-print "Good" "afternoon"))

(afternoon-greeter "Java" "community")
(afternoon-greeter "Clojure" "enthusiasts")

;; reader macro
;; it's like partial when you want to suply non last arg
;; please excuse the convoluted example
(def cheer #(concat-and-print %1 %2 "for" "the win"))

(cheer "Java" "community")
(cheer "Clojure" "enthusiasts")

;; composition
;; if you have pure functions you can compose them
(def negative-quotient (comp - /))
(negative-quotient 2 3)

;; any custom pure function is ok
(defn plus3
  [x]
  (+ x 3))

(plus3 0)

(defn times7
  [x]
  (* x 7))

(times7 3)

(def plus3times7 (comp times7 plus3))

(plus3times7 3)

;; complement
;; easily define a function with same effect but opposit boolean return value
(defn bad?
  [thing]
  (:bad thing))

;; I am going to rely on order of this HashMap now.
;; Lets hope it works
(def things
  [{:name "apple" :categoty "fruits" :bad false :count 20}
   {:name "Clojure" :categoty "programming languages" :bad false :count 1}
   {:name "flu" :categoty "illnesses" :bad true :count 100}
   {:name "knock knock" :categoty "jokes" :bad true :count 10000}])

(map bad? things)

(def good? (complement bad?))

(map good? things) 

;; thread last macro
;; let's say that we want to count good things now
(reduce +
        (map :count 
             (filter good? things)))
;; works fine but looks cryptic
;; is there a better way?
(->> things
     (filter good?)
     (map :count)
     (reduce +))

;; exception handling
(try
  (/ 1 0)
  (catch ArithmeticException e (println (str "You cannot " (. e getMessage)))))

;; runtime polymorphysm - defmulti lino bunny
;; we call seemingly the same method multiple times
;; but based on rules defined 
;; source: http://clojure.org/about/runtime_polymorphism
(defmulti encounter (fn [x y] [(:Species x) (:Species y)]))
(defmethod encounter [:Bunny :Lion] [b l] :run-away)
(defmethod encounter [:Lion :Bunny] [l b] :eat)
(defmethod encounter [:Lion :Lion] [l1 l2] :fight)
(defmethod encounter [:Bunny :Bunny] [b1 b2] :mate)
(def b1 {:Species :Bunny :other :stuff})
(def b2 {:Species :Bunny :other :stuff})
(def l1 {:Species :Lion :other :stuff})
(def l2 {:Species :Lion :other :stuff})
(encounter b1 b2)
(encounter b1 l1)
(encounter l1 b1)
(encounter l1 l2)














