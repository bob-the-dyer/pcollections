# pcollections

This project contains two set implementations based on:
  - an ordinary Binary Tree 
  - a Red-Black Tree  
  
Both persistent and non-persistent versions are supported.    
  
To build project and run unit tests you need maven to be preinstalled on your machine:
##### mvn clean package

### Main usage example
----
    PersistentSet<Integer> treeSet1 = new PersistentRedBlackTreeSet<>();
    
    PersistentSet<Integer> treeSet2 = treeSet1.insert(42);
    
    assert treeSet1.size() == 0;
    assert !treeSet1.contains(42);
    assert treeSet2.size() == 1;
    assert treeSet2.contains(42);
    
    PersistentSet<Integer> treeSet3 = treeSet2.remove(42);
    
    assert treeSet2.contains(42);
    assert treeSet2.size() == 1;
    assert !treeSet3.contains(42);
    assert treeSet3.size() == 0;
----
See unit tests for more examples of BinaryTreeSet, PersistentBinaryTreeSet, RedBlackTreeSet and PersistentRedBlackTreeSet usages.

### General aspects that were considered:

  - tree usage as an underlying structure for Set vs. Map -> Set
  - thread safety on modification / copying -> NO
  - elements of the set should be serializable(->YES), cloneable(->NO) and comparable(->YES) types
  - clone/copy objects on insert to preserve tree invariants consistency from external change of inserted elements -> YES
  - collection friendly (equals, hashCode) -> NO
  - null elements are allowed -> NO
  - collection should be serialisation ready -> YES

### Persistence aspects that were considered:

  - full persistence vs. partial -> full persistence
  - full copy on modification vs. minimal required copy of subtrees -> full copy
  
### Implementation road-map:

  - design, build and test binary tree based Set: contains, insert, remove, size, iterate - V
  - add persistence for binary tree and tests - V
  - "colorize" the Set: add insert/delete recoloring and rebalancing logic to keep RBTree invariants valid - V
  - add unit tests to prove RBTree invariants valid on insert/delete - V
  - add persistence for RBTree and tests - V
  - fair remove for RBTree with rebalancing - V  
  - benchmarking (?)
  - interfaces refactoring, adding convenience methods, see TODOs (?)
  - javadocs (?)
      
### Links for inspiration:

  - https://en.wikipedia.org/wiki/Red–black_tree
  - http://www.cs.cmu.edu/~sleator/papers/another-persistence.pdf
  - https://wiki.edinburghhacklab.com/PersistentRedBlackTreeSet
  - https://pcollections.org
  - https://github.com/google/guava