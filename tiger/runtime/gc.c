
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/*
测试方法：
java Tiger ../test/Sum.java -codegen C
java Tiger ../test/BinaryTree.java -codegen C
会自动编译,运行,记录log
a.out生成之后->./a.out 600
可以设置heap size，注意是以字节为单位


生成文件(a.c a.out gc_log)都在 tiger->bin目录下


*/



static void Tiger_gc ();

int allocationsize = 0;
int reclaimsize = 0;
int gctime = 1;
FILE* fp = NULL;
// The Gimple Garbage Collector.


//===============================================================//
// The Java Heap data structure.

/*   
      ----------------------------------------------------
      |                        |                         |
      ----------------------------------------------------
      ^\                      /^
      | \<~~~~~~~ size ~~~~~>/ |
    from                       to
 */
struct JavaHeap
{
  int size;         // in bytes, note that this if for semi-heap size
  char *from;       // the "from" space pointer
  //char *fromFree;   // the next "free" space in the from space
  char* fromScan;
  char* fromNext;
  char* fromLimit;

  char *to;         // the "to" space pointer
  //char *toStart;    // "start" address in the "to" space
  char *toScan;
  char *toNext;     // "next" free space pointer in the to space
  char* toLimit;
};

// The Java heap, which is initialized by the following
// "heap_init" function.
struct JavaHeap heap;

// Lab 4, exercise 10:
// Given the heap size (in bytes), allocate a Java heap
// in the C heap, initialize the relevant fields.
void Tiger_heap_init (int heapSize)
{
printf("\nSet Gimple Garbage Collector heapSize:%dbyte\n\n",Control_heapSize);

fp=fopen("gc_log","w+");
fclose(fp);



  // You should write 7 statement here:
  // #1: allocate a chunk of memory of size "heapSize" using "malloc"
void* p = malloc(heapSize);

  // #2: initialize the "size" field, note that "size" field
  // is for semi-heap, but "heapSize" is for the whole heap.
heap.size = heapSize/2;

  // #3: initialize the "from" field (with what value?)
heap.from = p;

  // #4: initialize the "fromFree" field (with what value?)
heap.fromNext = p;
heap.fromScan = p;

  // #5: initialize the "to" field (with what value?)
heap.to = p + heap.size;



  // #6: initizlize the "toStart" field with NULL;
heap.toScan = p + heap.size;

  // #7: initialize the "toNext" field with NULL;
heap.toNext = p + heap.size;



heap.fromLimit = heap.to;
heap.toLimit = p + heapSize;


  return;
}



//===============================================================//
// Object Model And allocation


// Lab 4: exercise 11:
// "new" a new object, do necessary initializations, and
// return the pointer (reference).
/*    ----------------
      | vptr      ---|----> (points to the virtual method table)
      |--------------|
      | isObjOrArray | (0: for normal objects)
      |--------------|
      | length       | (this field should be empty for normal objects)
      |--------------|
      | forwarding   | 
      |--------------|\
p---->| v_0          | \      
      |--------------|  s
      | ...          |  i
      |--------------|  z
      | v_{size-1}   | /e
      ----------------/
*/
// Try to allocate an object in the "from" space of the Java
// heap. Read Tiger book chapter 13.3 for details on the
// allocation.
// There are two cases to consider:
//   1. If the "from" space has enough space to hold this object, then
//      allocation succeeds, return the apropriate address (look at
//      the above figure, be careful);
//   2. if there is no enough space left in the "from" space, then
//      you should call the function "Tiger_gc()" to collect garbages.
//      and after the collection, there are still two sub-cases:
//        a: if there is enough space, you can do allocations just as case 1; 
//        b: if there is still no enough space, you can just issue
//           an error message ("OutOfMemory") and exit.
//           (However, a production compiler will try to expand
//           the Java heap.)

void* Tiger_new(void* vtable, int size)
{
  // You should write 4 statements for this function.
  // #1: "malloc" a chunk of memory of size "size":
  
  // #2: clear this chunk of memory (zero off it):
  
  // #3: set up the "vtable" pointer properly:
  
  // #4: return the pointer 


if(heap.fromNext + size >= heap.fromLimit)
{
printf("\nmemoryout,call garbage collector\n\n");


struct timeval start,end;
float timeuse;
gettimeofday(&start, NULL );
Tiger_gc ();
gettimeofday(&end, NULL );
timeuse = 1000000 * ( end.tv_sec - start.tv_sec ) + end.tv_usec - start.tv_usec;
timeuse /= 1000000;

fp=fopen("gc_log","at");
fprintf(fp,"%d round of Gimple Garbage Collector: %fs, collected %d bytes\r\n",gctime++,timeuse,allocationsize-reclaimsize);
fclose(fp);
allocationsize = 0;
reclaimsize = 0;
if(heap.fromNext + size >= heap.fromLimit)
{
printf("error\n");
exit(0);
}
}

printf("allocation:%dbytes ",size);

allocationsize += size;

void* vp = heap.fromNext;
*(int*)vp = vtable;//vp->vptr = vtable;
heap.fromNext += size;

printf(" Address:%p\n",vp);
 return vp;
  
}


// "new" an array of size "length", do necessary
// initializations. And each array comes with an
// extra "header" storing the array length and other information.
/*    ----------------
      | vptr         | (this field should be empty for an array)
      |--------------|
      | isObjOrArray | (1: for array)
      |--------------|
      | length       |
      |--------------|
      | forwarding   | 
      |--------------|\
p---->| e_0          | \      
      |--------------|  s
      | ...          |  i
      |--------------|  z
      | e_{length-1} | /e
      ----------------/
*/
// Try to allocate an array object in the "from" space of the Java
// heap. Read Tiger book chapter 13.3 for details on the
// allocation.
// There are two cases to consider:
//   1. If the "from" space has enough space to hold this array object, then
//      allocation succeeds, return the apropriate address (look at
//      the above figure, be careful);
//   2. if there is no enough space left in the "from" space, then
//      you should call the function "Tiger_gc()" to collect garbages.
//      and after the collection, there are still two sub-cases:
//        a: if there is enough space, you can do allocations just as case 1; 
//        b: if there is still no enough space, you can just issue
//           an error message ("OutOfMemory") and exit.
//           (However, a production compiler will try to expand
//           the Java heap.)
void *Tiger_new_array (int length)
{
  // Your code here:


  
}

//===============================================================//
// The Gimple Garbage Collector

// Lab 4, exercise 12:
// A copying collector based-on Cheney's algorithm.

extern void* prev;
void* lastprev = NULL;

void ChangePointer(char** ap,char** bp)
{

char* temp;
temp = (*ap);
(*ap) = (*bp);
(*bp) = temp;

}
static void Tiger_gc ()
{
  // Your code here:


int i=0;
void* traverprev=NULL;

char* cpArguments_gc_map;
char* cpLocals_gc_map;
char* cpField_gc_map;

int* forwardp;

int* localp;
int* referthisp;
int objectlength=0;

int* temp_gc_map;
int* temp_referthisp;
int* temp_forwardp;

traverprev = prev;


while(traverprev!=NULL && traverprev!=lastprev)
{
cpArguments_gc_map = (char*)*((int*)traverprev+2);
cpLocals_gc_map = cpArguments_gc_map + strlen(cpArguments_gc_map) + 1;
cpField_gc_map = (char*)*((int*)*((int*)*((int*)traverprev+1)));

memcpy((void*)heap.toNext,*((int*)traverprev+1),16+strlen(cpField_gc_map)*4);

*((int*)*((int*)traverprev+1)+3) = heap.toNext;
heap.toNext += 16+strlen(cpField_gc_map)*4;
printf("to-space add %d\n",16+strlen(cpField_gc_map)*4);

reclaimsize += 16+strlen(cpField_gc_map)*4;


localp = (int*)(cpLocals_gc_map + strlen(cpLocals_gc_map) + 1);




for(i=0;i<=strlen(cpLocals_gc_map);i++)
{
   referthisp = (int*)*(localp+i);
   if(referthisp >= heap.from && referthisp <= heap.fromLimit)
   {
      forwardp = (int*)*(referthisp+3);
      if(forwardp >= heap.from && forwardp <= heap.fromLimit)
      {
         cpField_gc_map = (char*)*((int*)(*referthisp));
         objectlength = 16 + strlen(cpField_gc_map) + 1;
         memcpy((void*)heap.toNext,referthisp,objectlength);
         forwardp = heap.toNext;
         *(localp+i) = forwardp;
         heap.toNext += objectlength;
         printf("to-space add %d\n",objectlength);
         reclaimsize += objectlength;
      }
      else
      {  
         *(localp+i) = forwardp;
         continue;
      }
   }
   else
   continue;
   
}



lastprev = traverprev;
traverprev = *((int*)traverprev);

}


while(heap.toScan != heap.toNext)
{



   referthisp = (int*)heap.toScan;
   localp = referthisp+4;
   cpField_gc_map = (char*)*((int*)(*referthisp));
   for(i=0;i<=strlen(cpField_gc_map);i++)
   {

       temp_referthisp = (int*)*(localp+i);
       if(temp_referthisp >= heap.from && temp_referthisp <= heap.fromLimit)
       {
      temp_forwardp = (int*)*(temp_referthisp+3);
      if(temp_forwardp >= heap.from && temp_forwardp <= heap.fromLimit)
      {
         temp_gc_map = (char*)*((int*)(*temp_referthisp));
         objectlength = 16 + strlen(temp_gc_map) + 1;
         memcpy((void*)heap.toNext,temp_referthisp,objectlength);
         temp_forwardp = heap.toNext;
         *(localp+i) = temp_forwardp;
         heap.toNext += objectlength;
         printf("to-space add %d\n",objectlength);


         reclaimsize += objectlength;
      }
      else
      {  
         *(localp+i) = temp_forwardp;
         continue;
      }
          
       }
       else
         continue;
   
   }

   heap.toScan += 16 + strlen(cpField_gc_map)*4;

}


//heap.to 不变
heap.toScan = heap.to;
//heap.toNext 不能变
//heap.toLimit不能变

//heap.from 不变
//heap.fromScan 不变
heap.fromNext = heap.from;
//heap.fromLimit 不变 


ChangePointer(&heap.from,&heap.to);
ChangePointer(&heap.fromScan,&heap.toScan);
ChangePointer(&heap.fromNext,&heap.toNext);
ChangePointer(&heap.fromLimit,&heap.toLimit);

}
//copyright by KIRA林泽南





