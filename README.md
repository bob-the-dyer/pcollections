# pcollections

This project contains a Persistent Set collection based on a Red/Black Tree implementation. 

To build project and run unit tests you need maven to be preinstalled on your machine:
#####mvn clean package

###General aspects that were considered:
  - Tree usage as an underlying structure for Set vs. Map -> Set
  - Thread safety on modification / copying -> NO
  - copy/deep copy/clone objects on insert to preserve tree invariants consistency from external change of inserted elements -> NO
  - collection friendly (equals, hashCode) -> ???
  - null elements are allowed -> NO

###Persistence implementation that was considered:
  - Full persistence vs. Partial -> Full persistence
  - Full copy on modification vs. minimal required copy of subtrees -> ???
  
###Implementation plan:
  - design, build and test binary tree based Set: contains, insert, remove, size, iterate
  - "colorize" the Set: add insert/delete recoloring and rebalancing logic to keep RBTree invariants valid
  - add unit tests to prove RBTree invariants valid on insert/delete  
  - add persistence and tests   
      
###Links for inspiration:
  - https://en.wikipedia.org/wiki/Red–black_tree
  - http://www.cs.cmu.edu/~sleator/papers/another-persistence.pdf
  - https://wiki.edinburghhacklab.com/PersistentRedBlackTreeSet
  - https://pcollections.org
  - https://github.com/google/guava
 
 

