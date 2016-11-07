(ns jc-clojure-demo.core
  (:gen-class))

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
(name a-function)

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

;; concurency
(defn long-running-function
  [a]
  (println "start")
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
;; composition
;; complement
;; reader macro

;; thread last macro
;; exception handling
;; java interop
;; runtime polymorphysm - defmulti lino bunny
