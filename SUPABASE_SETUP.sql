-- ═══════════════════════════════════════════════════════════
--  AZUEROMARKET — ESQUEMA SUPABASE
--  Ejecuta esto en: Supabase Dashboard → SQL Editor → New Query
-- ═══════════════════════════════════════════════════════════

-- ─── 1. EXTENSIONES ─────────────────────────────────────────
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ─── 2. TABLA: perfiles de usuario ──────────────────────────
-- Supabase maneja el login en auth.users.
-- Esta tabla extiende esos datos con información del negocio.
CREATE TABLE IF NOT EXISTS public.perfiles (
    id              UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    nombre          TEXT NOT NULL,
    tipo_usuario    TEXT NOT NULL CHECK (tipo_usuario IN ('EMPRENDEDOR','CLIENTE')),
    telefono        TEXT DEFAULT '',
    ubicacion       TEXT DEFAULT '',
    descripcion     TEXT DEFAULT '',
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- ─── 3. TABLA: productos ────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.productos (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    emprendedor_id      UUID NOT NULL REFERENCES public.perfiles(id) ON DELETE CASCADE,
    emprendedor_nombre  TEXT NOT NULL,
    nombre              TEXT NOT NULL,
    descripcion         TEXT DEFAULT '',
    precio              NUMERIC(10,2) NOT NULL CHECK (precio >= 0),
    categoria           TEXT NOT NULL,
    ubicacion           TEXT DEFAULT '',
    imagen_url          TEXT DEFAULT '',
    stock               INTEGER DEFAULT 0,
    disponible          BOOLEAN DEFAULT TRUE,
    created_at          TIMESTAMPTZ DEFAULT NOW()
);

-- ─── 4. TABLA: conversaciones ───────────────────────────────
-- Una conversación siempre tiene exactamente 2 participantes.
CREATE TABLE IF NOT EXISTS public.conversaciones (
    id                      UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cliente_id              UUID NOT NULL REFERENCES public.perfiles(id),
    emprendedor_id          UUID NOT NULL REFERENCES public.perfiles(id),
    producto_contexto_id    UUID REFERENCES public.productos(id),
    producto_contexto_nombre TEXT DEFAULT '',
    created_at              TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(cliente_id, emprendedor_id)
);

-- ─── 5. TABLA: mensajes ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.mensajes (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    conversacion_id  UUID NOT NULL REFERENCES public.conversaciones(id) ON DELETE CASCADE,
    remitente_id     UUID NOT NULL REFERENCES public.perfiles(id),
    contenido        TEXT NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT NOW()
);

-- ─── 6. TABLA: pedidos ──────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.pedidos (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cliente_id          UUID NOT NULL REFERENCES public.perfiles(id),
    cliente_nombre      TEXT NOT NULL,
    emprendedor_id      UUID NOT NULL REFERENCES public.perfiles(id),
    emprendedor_nombre  TEXT NOT NULL,
    subtotal            NUMERIC(10,2) NOT NULL,
    cargo_servicio      NUMERIC(10,2) NOT NULL,   -- 5% al cliente
    itbms               NUMERIC(10,2) NOT NULL,   -- 7% al cliente
    total               NUMERIC(10,2) NOT NULL,
    estado              TEXT DEFAULT 'PENDIENTE'
                        CHECK (estado IN ('PENDIENTE','CONFIRMADO','EN_PROCESO','ENTREGADO','CANCELADO')),
    nota                TEXT DEFAULT '',
    created_at          TIMESTAMPTZ DEFAULT NOW()
);

-- ─── 7. TABLA: items de pedido ──────────────────────────────
CREATE TABLE IF NOT EXISTS public.pedido_items (
    id           UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    pedido_id    UUID NOT NULL REFERENCES public.pedidos(id) ON DELETE CASCADE,
    producto_id  UUID NOT NULL REFERENCES public.productos(id),
    nombre       TEXT NOT NULL,          -- snapshot del nombre al momento de compra
    precio       NUMERIC(10,2) NOT NULL, -- snapshot del precio
    cantidad     INTEGER NOT NULL DEFAULT 1,
    subtotal     NUMERIC(10,2) NOT NULL
);

-- ═══════════════════════════════════════════════════════════
--  ROW LEVEL SECURITY (RLS) — Seguridad por filas
-- ═══════════════════════════════════════════════════════════

-- Habilitar RLS en todas las tablas
ALTER TABLE public.perfiles       ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.productos      ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.conversaciones ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.mensajes       ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.pedidos        ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.pedido_items   ENABLE ROW LEVEL SECURITY;

-- PERFILES: cada usuario lee/edita solo el suyo; todos leen nombre/tipo
CREATE POLICY "perfiles_select_all"  ON public.perfiles FOR SELECT USING (true);
CREATE POLICY "perfiles_insert_own"  ON public.perfiles FOR INSERT WITH CHECK (auth.uid() = id);
CREATE POLICY "perfiles_update_own"  ON public.perfiles FOR UPDATE USING (auth.uid() = id);

-- PRODUCTOS: cualquiera puede leer; solo el emprendedor dueño puede escribir
CREATE POLICY "productos_select_all"    ON public.productos FOR SELECT USING (true);
CREATE POLICY "productos_insert_own"    ON public.productos FOR INSERT
    WITH CHECK (auth.uid() = emprendedor_id);
CREATE POLICY "productos_update_own"    ON public.productos FOR UPDATE
    USING (auth.uid() = emprendedor_id);
CREATE POLICY "productos_delete_own"    ON public.productos FOR DELETE
    USING (auth.uid() = emprendedor_id);

-- CONVERSACIONES: solo los participantes pueden leer/crear
CREATE POLICY "conv_select_participant" ON public.conversaciones FOR SELECT
    USING (auth.uid() = cliente_id OR auth.uid() = emprendedor_id);
CREATE POLICY "conv_insert_participant" ON public.conversaciones FOR INSERT
    WITH CHECK (auth.uid() = cliente_id OR auth.uid() = emprendedor_id);

-- MENSAJES: solo los participantes de la conversación pueden leer/escribir
CREATE POLICY "msg_select_participant"  ON public.mensajes FOR SELECT
    USING (
        EXISTS (
            SELECT 1 FROM public.conversaciones c
            WHERE c.id = conversacion_id
            AND (c.cliente_id = auth.uid() OR c.emprendedor_id = auth.uid())
        )
    );
CREATE POLICY "msg_insert_remitente"    ON public.mensajes FOR INSERT
    WITH CHECK (auth.uid() = remitente_id);

-- PEDIDOS: cliente ve sus pedidos; emprendedor ve los que le llegan
CREATE POLICY "pedidos_select_own"      ON public.pedidos FOR SELECT
    USING (auth.uid() = cliente_id OR auth.uid() = emprendedor_id);
CREATE POLICY "pedidos_insert_cliente"  ON public.pedidos FOR INSERT
    WITH CHECK (auth.uid() = cliente_id);
CREATE POLICY "pedidos_update_emp"      ON public.pedidos FOR UPDATE
    USING (auth.uid() = emprendedor_id); -- emprendedor actualiza estado

-- PEDIDO ITEMS: siguen al pedido
CREATE POLICY "items_select_own"        ON public.pedido_items FOR SELECT
    USING (
        EXISTS (
            SELECT 1 FROM public.pedidos p
            WHERE p.id = pedido_id
            AND (p.cliente_id = auth.uid() OR p.emprendedor_id = auth.uid())
        )
    );
CREATE POLICY "items_insert_cliente"    ON public.pedido_items FOR INSERT
    WITH CHECK (
        EXISTS (
            SELECT 1 FROM public.pedidos p
            WHERE p.id = pedido_id AND p.cliente_id = auth.uid()
        )
    );

-- ═══════════════════════════════════════════════════════════
--  REALTIME — Habilitar para chat en tiempo real
-- ═══════════════════════════════════════════════════════════
-- En Supabase Dashboard → Database → Replication → Supabase Realtime
-- Activa las tablas: mensajes, conversaciones, pedidos

-- ═══════════════════════════════════════════════════════════
--  DATOS DE PRUEBA (opcional para desarrollo)
-- ═══════════════════════════════════════════════════════════
-- NOTA: Primero registra los usuarios desde la app, luego
-- usa sus UUIDs reales en vez de estos placeholders.

/*
-- Ejemplo (reemplaza los UUIDs con los reales de auth.users):
INSERT INTO public.perfiles (id, nombre, tipo_usuario, telefono, ubicacion, descripcion)
VALUES
  ('UUID_EMPRENDEDOR_1', 'María Rodríguez', 'EMPRENDEDOR', '+507 6123-4567', 'Las Tablas, Los Santos', 'Artesana 15 años de experiencia'),
  ('UUID_CLIENTE_1',     'Carlos Méndez',   'CLIENTE',     '+507 6987-6543', 'Ciudad de Panamá', '');

INSERT INTO public.productos (emprendedor_id, emprendedor_nombre, nombre, descripcion, precio, categoria, ubicacion, stock)
VALUES
  ('UUID_EMPRENDEDOR_1', 'María Rodríguez', 'Sombrero Pintado de Azuero',
   'Hecho a mano con fibra de bellota. Diseño tradicional de Los Santos.',
   85.00, 'ARTESANIAS', 'Las Tablas, Los Santos', 5);
*/
