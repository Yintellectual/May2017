ThreadPoolExecutor is a better choice then CachedThreadPool and FixedThreadPool.

ThreadPoolExecutor takes in 5 parameters:

	int core_thread_pool_size
	int max_thread_pool_size
	int time_to_kill_idel_thread //only when exceeding core thread pool size
	TimeUnit
	RejectHandler