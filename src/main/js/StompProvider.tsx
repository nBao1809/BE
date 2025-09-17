// ...existing code...
  useEffect(() => {
    console.log('[StompProvider] MOUNT');
    let client: any = null;
    let active = true;

    const cleanupClient = () => {
      try { if (client) client.deactivate(); } catch (e) { }
      client = null;
      setIsConnected(false);
      setItemsMap({});
      console.log('[StompProvider] CLEANUP & DEACTIVATE');
    };

    if (!sessionId) {
      cleanupClient();
      return;
    }

    (async () => {
      try {
        const wsUrl = getWsBase();
        console.log('[StompProvider] Preparing to connect to', wsUrl, 'with sessionId', sessionId);
        client = new Client({
          reconnectDelay: 5000,
          debug: (str) => console.log('[STOMP DEBUG]', str),
          webSocketFactory: () => {
            console.log('[StompProvider] Creating WebSocket to', wsUrl);
            return new WebSocket(wsUrl);
          },
        });
        console.log('[StompProvider] Activating STOMP client...');

        client.onConnect = () => {
          console.log('[StompProvider] STOMP connected!');
          setIsConnected(true);
          try {
            const topic1 = `/topic/session/${sessionId}/service-request`;
            const topic2 = `/topic/session/${sessionId}/order-detail`;
            const topic3 = `/topic/session/${sessionId}`;
            console.log('[WebSocket] SUBSCRIBE: session=' + sessionId + ', destination=' + topic1);
            client.subscribe(topic1, (m: any) => {
              try {
                const payload = JSON.parse(m.body);
                console.log('[Stomp] service-request message', payload);
                if (payload == null || payload.id == null) return;
                const rawId = payload.id;
                const id = String(rawId);
                setItemsMap(prev => {
                  const item = {
                    id,
                    name: payload.serviceTypeName ?? payload.name ?? 'Service',
                    quantity: payload.quantity ?? 1,
                    note: payload.note ?? undefined,
                    status: payload.status ?? 'PENDING',
                    type: 'service' as const,
                    tableNumber: payload.tableNumber != null ? String(payload.tableNumber) : (payload.tableNumber === 0 ? '0' : undefined),
                    orderId: undefined,
                    createdAt: payload.createdAt ?? payload.created_at ?? undefined,
                    handleTime: payload.handleTime ?? payload.handle_time ?? undefined,
                  } as StompProcessingItem;
                  const next = { ...prev, [id]: item };
                  return next;
                });
              } catch (e) { console.error('[Stomp] Error parsing service-request message', e); }
            });
            console.log('[WebSocket] SUBSCRIBE: session=' + sessionId + ', destination=' + topic2);
            client.subscribe(topic2, (m: any) => {
              try {
                const payload = JSON.parse(m.body);
                console.log('[Stomp] order-detail message', payload);
                const rawOrderId = payload.orderId ?? payload.order_id;
                const rawDetailId = payload.orderDetailId ?? payload.order_detail_id ?? payload.id;
                if (rawOrderId == null || rawDetailId == null) return;
                const orderId = String(rawOrderId);
                const detailId = String(rawDetailId);
                const id = `${orderId}-${detailId}`;
                setItemsMap(prev => {
                  const item = {
                    id,
                    name: payload.dishName ?? payload.dish_name ?? payload.dishname ?? 'Dish',
                    quantity: payload.quantity ?? 1,
                    note: payload.note ?? undefined,
                    status: payload.status ?? payload.orderStatus ?? 'PENDING',
                    type: 'dish' as const,
                    tableNumber: payload.tableName ?? (payload.tableNumber != null ? String(payload.tableNumber) : undefined),
                    orderId: orderId,
                    orderStatus: payload.orderStatus ?? undefined,
                    createdAt: payload.createdAt ?? payload.created_at ?? undefined,
                  } as StompProcessingItem;
                  const next = { ...prev, [id]: item };
                  return next;
                });
              } catch (e) { console.error('[Stomp] Error parsing order-detail message', e); }
            });
            console.log('[WebSocket] SUBSCRIBE: session=' + sessionId + ', destination=' + topic3);
            client.subscribe(topic3, (m: any) => {
              try {
                const payload = JSON.parse(m.body);
                console.log('[Stomp] session message', payload);
                const status = (payload.status || '').toString().toLowerCase();
                if (['ended', 'closed', 'paid', 'finished'].includes(status)) {
                  try { AppEvents.dispatchEvent('session-ended'); } catch (e) { }
                }
              } catch (e) { console.error('[Stomp] Error parsing session message', e); }
            });
          } catch (e) { console.error('[StompProvider] Error in onConnect', e); }
        };

        client.onStompError = (frame: any) => {
          setIsConnected(false);
          console.error('[StompProvider] STOMP error:', frame);
        };

        client.onWebSocketError = (event: any) => {
          setIsConnected(false);
          console.error('[StompProvider] WebSocket error:', event);
        };

        client.onWebSocketClose = (event: any) => {
          setIsConnected(false);
          console.warn('[StompProvider] WebSocket closed:', event);
        };

        client.activate();
        console.log('[StompProvider] STOMP client activated.');
      } catch (e) {
        console.error('[StompProvider] Error in WebSocket connection setup', e);
      }
    })();

    return () => {
      active = false;
      try { if (client) client.deactivate(); } catch (e) { }
      client = null;
      setIsConnected(false);
      console.log('[StompProvider] UNMOUNT & DEACTIVATE');
    };
  }, [sessionId]);
// ...existing code...
