# pcollections

This project contains a Persistent Set implementations based on:
  - an ordinary binary tree 
  - a Red-Black tree

To build project and run unit tests you need maven to be preinstalled on your machine:
#####mvn clean package

###Main usage example
TODO here to add 3 lines of code for PersistentRedBlackTreeSet usage
See unit tests for more examples of PersistentRedBlackTreeSet usage

###General aspects that were considered:
  - Tree usage as an underlying structure for Set vs. Map -> Set
  - Thread safety on modification / copying -> NO
  - elements of the set should be serializable and comparable types -> YES
  - copy/deep copy/clone objects on insert to preserve tree invariants consistency from external change of inserted elements -> NO
  - collection friendly (equals, hashCode) -> NO
  - null elements are allowed -> NO
  - collection should be serialisation ready -> YES

###Persistence implementation that was considered:
  - Full persistence vs. Partial -> Full persistence
  - Full copy on modification vs. minimal required copy of subtrees -> Full copy
  
###Implementation plan:
  - design, build and test binary tree based Set: contains, insert, remove, size, iterate - V
  - add persistence for binary tree and tests - IN PROGRESS
  - "colorize" the Set: add insert/delete recoloring and rebalancing logic to keep RBTree invariants valid
  - add unit tests to prove RBTree invariants valid on insert/delete  
  - add persistence for RBTree and tests   
  - benchmarking (?)
      
###Links for inspiration:
  - https://en.wikipedia.org/wiki/Red–black_tree
  - http://www.cs.cmu.edu/~sleator/papers/another-persistence.pdf
  - https://wiki.edinburghhacklab.com/PersistentRedBlackTreeSet
  - https://pcollections.org
  - https://github.com/google/guava
 
 

