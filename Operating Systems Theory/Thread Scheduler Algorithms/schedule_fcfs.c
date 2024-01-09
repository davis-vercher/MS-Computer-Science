#include <stdio.h>

struct Task {
    char name[10];
    int priority;
    int cpu_burst;
};

void schedule_fcfs(struct Task tasks[], int n) {
    for (int i = 0; i < n; i++) {
        printf("Executing task %s with priority %d and CPU burst %d\n", tasks[i].name, tasks[i].priority, tasks[i].cpu_burst);
    }
}
