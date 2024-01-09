#include <stdio.h>

struct Task {
    char name[10];
    int priority;
    int cpu_burst;
};

void schedule_priority(struct Task tasks[], int n) {
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            if (tasks[i].priority < tasks[j].priority) {
                struct Task temp = tasks[i];
                tasks[i] = tasks[j];
                tasks[j] = temp;
            }
        }
    }

    for (int i = 0; i < n; i++) {
        printf("Executing task %s with priority %d\n", tasks[i].name, tasks[i].priority);
    }
}
