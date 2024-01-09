#include <stdio.h>
#include <stdbool.h>

struct Task {
    char name[10];
    int priority;
    int cpu_burst;
    bool executed;
};

void schedule_sjf(struct Task tasks[], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = i + 1; j < n; j++) {
            if (tasks[i].cpu_burst > tasks[j].cpu_burst) {
                struct Task temp = tasks[i];
                tasks[i] = tasks[j];
                tasks[j] = temp;
            }
        }
    }

    for (int i = 0; i < n; i++) {
        printf("Executing task %s with priority %d and CPU burst %d\n", tasks[i].name, tasks[i].priority, tasks[i].cpu_burst);
    }
}
